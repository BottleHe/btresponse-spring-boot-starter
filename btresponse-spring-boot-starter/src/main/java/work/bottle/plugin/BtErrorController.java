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
import work.bottle.plugin.model.BtResponse;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@ConditionalOnProperty(name = "bt-response.enable", matchIfMissing = true)
@RequestMapping("${server.error.path:${error.path:/____error}}")
public class BtErrorController extends AbstractErrorController {
    private static final Logger logger = LoggerFactory.getLogger(BtErrorController.class);

    private final ErrorProperties errorProperties;
    private final BtResponseProperties btResponseProperties;

    /**
     * Create a new {@link BtErrorController} instance.
     * @param errorAttributes the error attributes
     * @param errorProperties configuration properties
     * @param btResponseProperties btResponseProperties
     */
    public BtErrorController(ErrorAttributes errorAttributes, ErrorProperties errorProperties, BtResponseProperties btResponseProperties) {
        this(errorAttributes, errorProperties, btResponseProperties, Collections.emptyList());
    }

    /**
     * Create a new {@link BtErrorController} instance.
     * @param errorAttributes the error attributes
     * @param errorProperties configuration properties
     * @param btResponseProperties btResponseProperties
     * @param errorViewResolvers error view resolvers
     */
    public BtErrorController(ErrorAttributes errorAttributes, ErrorProperties errorProperties,
                             BtResponseProperties btResponseProperties,
                             List<ErrorViewResolver> errorViewResolvers) {
        super(errorAttributes, errorViewResolvers);
        Assert.notNull(errorProperties, "ErrorProperties must not be null");
        Assert.notNull(btResponseProperties, "BtResponseProperties must not be null");
        this.errorProperties = errorProperties;
        this.btResponseProperties = btResponseProperties;
    }

    @RequestMapping(produces = MediaType.TEXT_HTML_VALUE)
    public Object errorHtml(HttpServletRequest request, HttpServletResponse response) {
        logger.debug("properties: {}, isNull: {}", btResponseProperties, null == btResponseProperties);
        if ((null == btResponseProperties.getEnable() || btResponseProperties.getEnable())
                && null != btResponseProperties.getForce() && btResponseProperties.getForce()) {
            logger.debug("force to return BtResponse json error");
            return error(request);
        }
        logger.error("[Out of springboot exception]:\n{} -> [{}]:{}", request.getLocalAddr(), request.getMethod(), request.getRequestURL());
        HttpStatus status = getStatus(request);
        Map<String, Object> model = Collections
                .unmodifiableMap(getErrorAttributes(request, getErrorAttributeOptions(request, MediaType.TEXT_HTML)));
        response.setStatus(status.value());
        ModelAndView modelAndView = resolveErrorView(request, response, status, model);
        return (modelAndView != null) ? modelAndView : new ModelAndView("error", model);
    }

    @RequestMapping
    public ResponseEntity<BtResponse> error(HttpServletRequest request) {
        logger.error("[Out of springboot exception]:\n{} -> [{}]:{}", request.getLocalAddr(), request.getMethod(), request.getRequestURL());
        Throwable e = (Throwable) request.getAttribute(RequestDispatcher.ERROR_EXCEPTION);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        if (null != e) {
            logger.error("", e);
            Throwable cause = e.getCause();
            if (null != cause) {
                // logger.error("  -- {}", cause);
                if (cause instanceof OperationException) {
                    return new ResponseEntity<>(new BtResponse(false, ((OperationException) cause).getCode(),
                            ((OperationException) cause).getData(), cause.getMessage()), HttpStatus.OK);
                }
            }
        }
        Map<String, Object> body = getErrorAttributes(request, getErrorAttributeOptions(request, MediaType.ALL));
        HttpStatus status = getStatus(request);
        BtResponse ret = new BtResponse(false, status.value(), body, (String) body.getOrDefault("error", "Internal server error"));
        body.remove("error");
        body.remove("status");
        return new ResponseEntity<>(ret, headers, status);
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
