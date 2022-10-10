package work.bottle.plugin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
import work.bottle.plugin.annotation.Ignore;
import work.bottle.plugin.exception.OperationException;
import work.bottle.plugin.exception.ServerException;


@ControllerAdvice
@ConditionalOnProperty(name = "bt-response.enable", matchIfMissing = true)
public class BtResponseBodyAdvice implements ResponseBodyAdvice<Object> {
    private static final Logger logger = LoggerFactory.getLogger(BtResponseBodyAdvice.class);

    private final StandardResponseFactory standardResponseFactory;

    public BtResponseBodyAdvice(StandardResponseFactory standardResponseFactory) {
        this.standardResponseFactory = standardResponseFactory;
    }

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        logger.debug("BtResponseBodyAdvice::supports({}, {})", returnType, converterType);
        if (AnnotatedElementUtils.hasAnnotation(returnType.getContainingClass(), Ignore.class)
                || returnType.hasMethodAnnotation(Ignore.class)) {
            return false;
        }
//        logger.debug("Converter is instance of Class [{}] ? {}", MappingJackson2HttpMessageConverter.class.getName(),
//                MappingJackson2HttpMessageConverter.class.isAssignableFrom(converterType));
        if (!MappingJackson2HttpMessageConverter.class.isAssignableFrom(converterType)) {
            return false;
        }

        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  ServerHttpRequest request, ServerHttpResponse response) {
        logger.debug("BtResponseBodyAdvice::beforeBodyWrite({}, {}, {}, {}, {}, {})", body, returnType, selectedContentType, selectedConverterType, request, response);
        try {
            if (null == body) {
                logger.debug("body is null");
                return standardResponseFactory.produceDefaultResponse();
            }
            if (standardResponseFactory.isInstance(body)) {
                return body;
            }
            logger.debug("body type: {}" + body.getClass().getName());
            return standardResponseFactory.produceResponse(0, StandardResponseFactory.EMPTY_STR, body);
        } catch (Throwable t) {
            logger.warn("[Springboot ExceptionHandler]", t);
            if (t instanceof OperationException)
            {
                return standardResponseFactory.produceErrorResponse((OperationException) t);
            } else if (t instanceof ServerException) {
                return standardResponseFactory.produceErrorResponse((ServerException) t);
            }
            return standardResponseFactory.produceResponse(500, "Internal Server Error", null);
        }
    }
}
