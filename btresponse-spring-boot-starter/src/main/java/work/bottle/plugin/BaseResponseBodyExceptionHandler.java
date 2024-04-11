package work.bottle.plugin;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import work.bottle.plugin.annotation.Ignore;
import work.bottle.plugin.exception.GlobalException;
import work.bottle.plugin.exception.OperationException;

import org.springframework.validation.BindException;
import work.bottle.plugin.exception.global.client.UnprocessableException;
import work.bottle.plugin.exception.global.client.UnsupportedException;

import java.util.List;
import java.util.Objects;

/**
 * 内部异常处理类, 针对一些特定的异常进行统一处理
 */
@ControllerAdvice
@ConditionalOnProperty(name = "bt-response.enable", matchIfMissing = true)
public class BaseResponseBodyExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(BaseResponseBodyExceptionHandler.class);

    private final StandardResponseFactory standardResponseFactory;
    private final BtResponseProperties btResponseProperties;
    private final RequestMappingHandlerMapping handlerMapping;
    private final HttpServletRequest httpServletRequest;

    public BaseResponseBodyExceptionHandler(StandardResponseFactory standardResponseFactory,
                                            BtResponseProperties btResponseProperties,
                                            RequestMappingHandlerMapping handlerMapping,
                                            HttpServletRequest httpServletRequest) {
        this.standardResponseFactory = standardResponseFactory;
        this.btResponseProperties = btResponseProperties;
        this.handlerMapping = handlerMapping;
        this.httpServletRequest = httpServletRequest;
    }

    /**
     * 参数绑定异常, 处理方式同 work.bottle.plugin.exception.global.base.UnprocessableException
     * error code 422
     *
     * @param e        异常内容
     * @param response
     * @return
     */
    @ExceptionHandler(BindException.class)
    @ResponseBody
    public ResponseEntity bindExceptionHandler(BindException e, HttpServletResponse response) throws Throwable {
        logger.warn("[BindExceptionHandler]", e);
        // 清空response body, 不清除的话. 会出现里面存在两个JSON的情况.
        response.reset();
        BindingResult bindingResult = e.getBindingResult();
        if (bindingResult.hasErrors()) {
            List<ObjectError> errors = bindingResult.getAllErrors();
            if (errors != null) {
                if (0 < errors.size()) {
                    FieldError fieldError = (FieldError) errors.get(0);
                    return standardResponseFactory.produceResponseEntity(false, UnprocessableException.Default.getCode(),
                            fieldError.getDefaultMessage(), null,
                            btResponseProperties.isLooseMode() /* 宽松模式时, 都应该是 200 */
                                    ? HttpStatus.OK.value()
                                    : UnprocessableException.Default.getCode(),
                            null);
                }
            }
        }
        return standardResponseFactory.produceResponseEntity(false, UnprocessableException.Default.getCode(),
                e.getMessage(), null,
                btResponseProperties.isLooseMode() /* 宽松模式时, 都应该是 200 */
                        ? HttpStatus.OK.value()
                        : UnprocessableException.Default.getCode(),
                null);
    }

    /**
     * 参数验证异常 基于 javax. 处理方式同 work.bottle.plugin.exception.global.base.UnsupportedException
     * error code 415
     *
     * @param e
     * @param response
     * @return
     */
    @ExceptionHandler(ValidationException.class)
    @ResponseBody
    public ResponseEntity validationExceptionHandler(ValidationException e, HttpServletResponse response) throws Throwable {
        logger.warn("[ValidationExceptionHandler]", e);
        // 清空response body, 不清除的话. 会出现里面存在两个JSON的情况.
        response.reset();
        return standardResponseFactory.produceResponseEntity(false, UnsupportedException.Default.getCode(),
                e.getMessage(), null,
                btResponseProperties.isLooseMode() /* 宽松模式时, 都应该是 200 */
                        ? HttpStatus.OK.value()
                        : UnsupportedException.Default.getCode(),
                null);
    }

    /**
     * 全局异常, 自带一个http status 或 code
     *
     * @param e
     * @param response
     * @return
     */
    @ExceptionHandler(GlobalException.class)
    @ResponseBody
    public ResponseEntity globalExceptionHandler(GlobalException e,
                                                 HttpServletResponse response) throws Throwable {
        response.reset();
        return standardResponseFactory.produceResponseEntity(false, e.getCode(),
                e.getMessage(), e.getData(),
                btResponseProperties.isLooseMode() /* 宽松模式时, 都应该是 200 */
                        ? HttpStatus.OK.value()
                        : e.getCode(),
                null);
    }

    /**
     * 业务异常处理. 返回http status固定为200. code表示异常内容
     *
     * @param e
     * @param response
     * @return
     */
    @ExceptionHandler(OperationException.class)
    @ResponseBody
    public ResponseEntity operationExceptionHandler(OperationException e,
                                                    HttpServletResponse response) throws Throwable {
        response.reset();
        return standardResponseFactory.produceResponseEntity(false, e.getCode(),
                e.getMessage(), e.getData(), HttpStatus.OK.value(), null);

    }

//    @ExceptionHandler(ServletException.class)
//    @ResponseBody
//    public ResponseEntity servletExceptionHandler(ServletException e,
//                                                  HttpServletRequest request, HttpServletResponse response) {
//        logger.warn("[ServletExceptionHandler]", e);
//        // 清空response body, 不清除的话. 会出现里面存在两个JSON的情况.
//        response.reset();
//
//        return standardResponseFactory.produceResponseEntity(false, ContextException.Default.getCode(),
//                e.getMessage(), null,
//                btResponseProperties.isLooseMode()
//                        ? HttpStatus.OK.value()
//                        : ContextException.Default.getCode(),
//                null);
//    }

    // 很多异常涉及HTTP返回, 暂时不再处理它
//    @ExceptionHandler(Throwable.class)
//    @ResponseBody
//    public ResponseEntity baseExceptionHandler(Throwable t, HttpServletRequest request, HttpServletResponse response) {
//        logger.warn("[Springboot Default ExceptionHandler]", t);
//        // 清空response body, 不清除的话. 会出现里面存在两个JSON的情况.
//        // int status = response.getStatus();
//        response.reset();
//        return standardResponseFactory.produceErrorResponseEntity(t, HttpStatus.INTERNAL_SERVER_ERROR.value());
//    }

    /**
     * 这个方法是针对方法上或类上注明了需要忽略的 注解 "@Ignore" 时, 返回信息直接忽略, 但是实际生产中, 这类方法没有实际意义.
     * 所以废弃这个函数
     * @param t
     * @param success
     * @param code
     * @param message
     * @param data
     * @param status
     * @param headers
     * @return
     * @throws Throwable
     */
    @Deprecated
    private ResponseEntity processResponse(Throwable t, boolean success, int code, String message,
                                           Object data, int status, MultiValueMap<String, String> headers) throws Throwable {
        try {
            Object handler = Objects.requireNonNull(handlerMapping.getHandler(httpServletRequest)).getHandler();
            if (handler instanceof HandlerMethod) {
                if (((HandlerMethod) handler).hasMethodAnnotation(Ignore.class)
                        || null != AnnotationUtils.findAnnotation(((HandlerMethod) handler).getMethod().getDeclaringClass(), Ignore.class)) {
                    throw t;
                }
            }
        } catch (Exception e) {
            logger.warn("ProcessResponse", e);
        }
        return standardResponseFactory.produceResponseEntity(success, code,
                message, data, status, headers);
    }
}
