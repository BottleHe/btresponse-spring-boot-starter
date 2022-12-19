package work.bottle.plugin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MissingRequestValueException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import work.bottle.plugin.exception.OperationException;
import work.bottle.plugin.exception.ServerException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ValidationException;
import org.springframework.validation.BindException;

import java.util.List;
import java.util.Optional;

@ControllerAdvice
@ConditionalOnProperty(name = "bt-response.enable", matchIfMissing = true)
public class BaseResponseBodyExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(BaseResponseBodyExceptionHandler.class);

    private final StandardResponseFactory standardResponseFactory;

    public BaseResponseBodyExceptionHandler(StandardResponseFactory standardResponseFactory) {
        this.standardResponseFactory = standardResponseFactory;
    }

    @ExceptionHandler(BindException.class)
    @ResponseBody
    public ResponseEntity bindExceptionHandler(BindException e, HttpServletRequest request, HttpServletResponse response) {
        logger.warn("[BindExceptionHandler]", e);
        // 清空response body, 不清除的话. 会出现里面存在两个JSON的情况.
        response.reset();
        BindingResult bindingResult = e.getBindingResult();
        if (bindingResult.hasErrors()) {
            List<ObjectError> errors = bindingResult.getAllErrors();
            if (errors != null) {
                if (0 < errors.size()) {
                    FieldError fieldError = (FieldError) errors.get(0);
                    return standardResponseFactory.produceResponseEntity(false, 10004,
                            fieldError.getDefaultMessage(), null, HttpStatus.OK, null);
                }
            }
        }
        return standardResponseFactory.produceResponseEntity(false, 10004,
                e.getMessage(), null, HttpStatus.OK, null);
    }

    @ExceptionHandler(ValidationException.class)
    @ResponseBody
    public ResponseEntity validationExceptionHandler(ValidationException e, HttpServletRequest request, HttpServletResponse response) {
        logger.warn("[ValidationExceptionHandler]", e);
        // 清空response body, 不清除的话. 会出现里面存在两个JSON的情况.
        response.reset();
        return standardResponseFactory.produceResponseEntity(false, 10004,
                e.getMessage(), null, HttpStatus.OK, null);
    }

    @ExceptionHandler(ServerException.class)
    @ResponseBody
    public ResponseEntity serverExceptionHandler(ServerException e,
                                                                HttpServletRequest request,
                                                                HttpServletResponse response) {
        response.reset();
        return standardResponseFactory.produceResponseEntity(false, e.getCode(),
                e.getMessage(), e.getData(),
                Optional.ofNullable(HttpStatus.resolve(e.getCode())).orElse(HttpStatus.INTERNAL_SERVER_ERROR),
                null);
    }

    @ExceptionHandler(OperationException.class)
    @ResponseBody
    public ResponseEntity operationExceptionHandler(OperationException e,
                                                               HttpServletRequest request,
                                                               HttpServletResponse response) {
        response.reset();
        return standardResponseFactory.produceResponseEntity(false, e.getCode(),
                e.getMessage(), e.getData(), HttpStatus.OK, null);

    }

    @ExceptionHandler(MissingRequestValueException.class)
    @ResponseBody
    public ResponseEntity missingRequestValueExceptionHandler(MissingRequestValueException e,
                                                                          HttpServletRequest request, HttpServletResponse response) {
        logger.warn("[MissingRequestValueExceptionHandler]", e);
        // 清空response body, 不清除的话. 会出现里面存在两个JSON的情况.
        int status = response.getStatus();
        response.reset();

        return standardResponseFactory.produceResponseEntity(false, status,
                e.getMessage(), null, HttpStatus.resolve(status), null);
    }

    // 很多异常涉及HTTP返回, 暂时不再处理它
//    @ExceptionHandler(Throwable.class)
//    @ResponseBody
//    public ResponseEntity baseExceptionHandler(Throwable t, HttpServletRequest request, HttpServletResponse response) {
//        logger.warn("[Springboot Default ExceptionHandler]", t);
//        // 清空response body, 不清除的话. 会出现里面存在两个JSON的情况.
//        // int status = response.getStatus();
//        response.reset();
//        return standardResponseFactory.produceErrorResponseEntity(t, HttpStatus.INTERNAL_SERVER_ERROR);
//    }
}
