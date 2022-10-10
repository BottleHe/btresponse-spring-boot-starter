package work.bottle.plugin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.servlet.error.AbstractErrorController;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorViewResolver;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import work.bottle.plugin.exception.OperationException;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@Controller
@ConditionalOnProperty(name = "bt-response.enable", matchIfMissing = true)
@RequestMapping("${server.error.path:${error.path:/error}}")
public class BtErrorController extends AbstractErrorController {
    private static final Logger logger = LoggerFactory.getLogger(BtErrorController.class);

    private final ErrorProperties errorProperties;
    private final BtResponseProperties btResponseProperties;
    private final StandardResponseFactory standardResponseFactory;

    /**
     * Create a new {@link BtErrorController} instance.
     * @param errorAttributes the error attributes
     * @param errorProperties configuration properties
     * @param btResponseProperties btResponseProperties
     */
    public BtErrorController(StandardResponseFactory standardResponseFactory, ErrorAttributes errorAttributes, ErrorProperties errorProperties, BtResponseProperties btResponseProperties) {
        this(standardResponseFactory, errorAttributes, errorProperties, btResponseProperties, Collections.emptyList());
    }

    /**
     * Create a new {@link BtErrorController} instance.
     * @param errorAttributes the error attributes
     * @param errorProperties configuration properties
     * @param btResponseProperties btResponseProperties
     * @param errorViewResolvers error view resolvers
     */
    public BtErrorController(StandardResponseFactory standardResponseFactory,
                             ErrorAttributes errorAttributes, ErrorProperties errorProperties,
                             BtResponseProperties btResponseProperties,
                             List<ErrorViewResolver> errorViewResolvers) {
        super(errorAttributes, errorViewResolvers);
        Assert.notNull(errorProperties, "ErrorProperties must not be null");
        Assert.notNull(btResponseProperties, "BtResponseProperties must not be null");
        this.errorProperties = errorProperties;
        this.btResponseProperties = btResponseProperties;
        this.standardResponseFactory = standardResponseFactory;
    }

    @RequestMapping(produces = MediaType.TEXT_HTML_VALUE)
    public Object errorHtml(HttpServletRequest request, HttpServletResponse response) {
        logger.debug("properties: {}, isNull: {}", btResponseProperties, null == btResponseProperties);
        if ((null == btResponseProperties.getEnable() || btResponseProperties.getEnable())
                && null != btResponseProperties.getForce() && btResponseProperties.getForce()) {
            logger.debug("force to return BtResponse json error");
            return error(request);
        }
        logger.error("[Out of springboot exception]:\n{} -> [{}]:{}\n{} ({})", request.getRemoteHost(),
                request.getMethod(),
                request.getAttribute(RequestDispatcher.ERROR_REQUEST_URI),
                request.getAttribute(RequestDispatcher.ERROR_MESSAGE),
                request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE));
        HttpStatus status = getStatus(request);
        Map<String, Object> model = Collections
                .unmodifiableMap(getErrorAttributes(request, getErrorAttributeOptions(request, MediaType.TEXT_HTML)));
        response.setStatus(status.value());
        ModelAndView modelAndView = resolveErrorView(request, response, status, model);
        return (modelAndView != null) ? modelAndView : new ModelAndView("error", model);
    }

    @RequestMapping
    public ResponseEntity error(HttpServletRequest request) {
        logger.error("[Out of springboot exception]:\n{} -> [{}]:{}\n{} ({})", request.getRemoteHost(),
                request.getMethod(),
                request.getAttribute(RequestDispatcher.ERROR_REQUEST_URI),
                request.getAttribute(RequestDispatcher.ERROR_MESSAGE),
                request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE));
//        Enumeration<String> attributeNames = request.getAttributeNames();
//        while (attributeNames.hasMoreElements()) {
//            String s = attributeNames.nextElement();
//            logger.info("{}: {}", s, request.getAttribute(s));
//        }
        Throwable e = (Throwable) request.getAttribute(RequestDispatcher.ERROR_EXCEPTION);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        if (null != e) {
            logger.error("", e);
            Throwable cause = e.getCause();
            if (null != cause) {
                // logger.error("  -- {}", cause);
                if (cause instanceof OperationException) {
                    return standardResponseFactory.produceErrorResponseEntity((OperationException) cause);
                }
            }
        }
        Map<String, Object> body = getErrorAttributes(request, getErrorAttributeOptions(request, MediaType.ALL));
        HttpStatus status = getStatus(request);
        ResponseEntity responseEntity = standardResponseFactory.produceResponseEntity(false,
                status.value(), (String) body.getOrDefault("error", "Internal server error"), body, status, headers);

        body.remove("error");
        body.remove("status");
        return responseEntity;
    }

    @ExceptionHandler(HttpMediaTypeNotAcceptableException.class)
    public ResponseEntity<String> mediaTypeNotAcceptable(HttpServletRequest request) {
        HttpStatus status = getStatus(request);
        return ResponseEntity.status(status).build();
    }

    protected ErrorAttributeOptions getErrorAttributeOptions(HttpServletRequest request, MediaType mediaType) {
        ErrorAttributeOptions options = ErrorAttributeOptions.defaults();
        if (this.errorProperties.isIncludeException()) {
            options = options.including(ErrorAttributeOptions.Include.EXCEPTION);
        }
        if (isIncludeStackTrace(request, mediaType)) {
            options = options.including(ErrorAttributeOptions.Include.STACK_TRACE);
        }
        if (isIncludeMessage(request, mediaType)) {
            options = options.including(ErrorAttributeOptions.Include.MESSAGE);
        }
        if (isIncludeBindingErrors(request, mediaType)) {
            options = options.including(ErrorAttributeOptions.Include.BINDING_ERRORS);
        }
        return options;
    }

    /**
     * Determine if the stacktrace attribute should be included.
     * @param request the source request
     * @param produces the media type produced (or {@code MediaType.ALL})
     * @return if the stacktrace attribute should be included
     */
    protected boolean isIncludeStackTrace(HttpServletRequest request, MediaType produces) {
        switch (getErrorProperties().getIncludeStacktrace()) {
            case ALWAYS:
                return true;
            case ON_PARAM:
                return getTraceParameter(request);
            default:
                return false;
        }
    }

    /**
     * Determine if the message attribute should be included.
     * @param request the source request
     * @param produces the media type produced (or {@code MediaType.ALL})
     * @return if the message attribute should be included
     */
    protected boolean isIncludeMessage(HttpServletRequest request, MediaType produces) {
        switch (getErrorProperties().getIncludeMessage()) {
            case ALWAYS:
                return true;
            case ON_PARAM:
                return getMessageParameter(request);
            default:
                return false;
        }
    }

    /**
     * Determine if the errors attribute should be included.
     * @param request the source request
     * @param produces the media type produced (or {@code MediaType.ALL})
     * @return if the errors attribute should be included
     */
    protected boolean isIncludeBindingErrors(HttpServletRequest request, MediaType produces) {
        switch (getErrorProperties().getIncludeBindingErrors()) {
            case ALWAYS:
                return true;
            case ON_PARAM:
                return getErrorsParameter(request);
            default:
                return false;
        }
    }

    /**
     * Provide access to the error properties.
     * @return the error properties
     */
    protected ErrorProperties getErrorProperties() {
        return this.errorProperties;
    }
}
