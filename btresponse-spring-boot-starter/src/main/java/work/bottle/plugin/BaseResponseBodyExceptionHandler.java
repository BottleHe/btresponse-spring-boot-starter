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
import work.bottle.plugin.model.BtResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ValidationException;
import org.springframework.validation.BindException;

import java.util.List;

@ControllerAdvice
@ConditionalOnProperty(name = "bt-response.enable", matchIfMissing = true)
public class BaseResponseBodyExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(BaseResponseBodyExceptionHandler.class);

    @ExceptionHandler(BindException.class)
    @ResponseBody
    public ResponseEntity<BtResponse> bindExceptionHandler(BindException e, HttpServletRequest request, HttpServletResponse response) {
        logger.warn("[BindExceptionHandler]", e);
        // 清空response body, 不清除的话. 会出现里面存在两个JSON的情况.
        response.reset();
        BindingResult bindingResult = e.getBindingResult();
        if (bindingResult.hasErrors()) {
            List<ObjectError> errors = bindingResult.getAllErrors();
            if (errors != null) {
                if (0 < errors.size()) {
                    FieldError fieldError = (FieldError) errors.get(0);
                    return new ResponseEntity<>(new BtResponse(false, 100004,
                            null, fieldError.getDefaultMessage()), HttpStatus.OK);
                }
            }
        }
        return new ResponseEntity<>(new BtResponse(false, 100004,
                null, e.getMessage()), HttpStatus.OK);
    }

    @ExceptionHandler(ValidationException.class)
    @ResponseBody
    public ResponseEntity<BtResponse> validationExceptionHandler(ValidationException e, HttpServletRequest request, HttpServletResponse response) {
        logger.warn("[ValidationExceptionHandler]", e);
        // 清空response body, 不清除的话. 会出现里面存在两个JSON的情况.
        response.reset();
        return new ResponseEntity<>(new BtResponse(false, 100004,
                null, e.getMessage()), HttpStatus.OK);
    }

    @ExceptionHandler(OperationException.class)
    @ResponseBody
    public ResponseEntity<BtResponse> operationExceptionHandler(OperationException e,
                                                               HttpServletRequest request,
                                                               HttpServletResponse response) {

        return new ResponseEntity<>(new BtResponse(false, e.getCode(),
                e.getData(), e.getMessage()), HttpStatus.OK);
    }

    @ExceptionHandler(MissingRequestValueException.class)
    @ResponseBody
    public ResponseEntity<BtResponse> missingRequestValueExceptionHandler(MissingRequestValueException e,
                                                                          HttpServletRequest request, HttpServletResponse response) {
        logger.warn("[MissingRequestValueExceptionHandler]", e);
        // 清空response body, 不清除的话. 会出现里面存在两个JSON的情况.
        response.reset();
        return new ResponseEntity<>(new BtResponse(response.getStatus(), e.getMessage()), HttpStatus.resolve(response.getStatus()));
    }

    @ExceptionHandler(Throwable.class)
    @ResponseBody
    public ResponseEntity<BtResponse> baseExceptionHandler(Throwable t, HttpServletRequest request, HttpServletResponse response) {
        logger.warn("[Springboot Default ExceptionHandler]", t);
        // 清空response body, 不清除的话. 会出现里面存在两个JSON的情况.
        response.reset();
        return new ResponseEntity<>(new BtResponse(response.getStatus(), "Internal Server Error"), HttpStatus.resolve(response.getStatus()));
    }
}
