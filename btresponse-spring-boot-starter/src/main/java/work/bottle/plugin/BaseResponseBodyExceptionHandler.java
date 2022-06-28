package work.bottle.plugin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import work.bottle.plugin.exception.OperationException;
import work.bottle.plugin.model.BtResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

// @ControllerAdvice
@ConditionalOnProperty(name = "bt-response.enable", matchIfMissing = true)
public class BaseResponseBodyExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(BaseResponseBodyExceptionHandler.class);

    @ExceptionHandler(Throwable.class)
    @ResponseBody
    public ResponseEntity<BtResponse> baseExceptionHandler(Throwable t, HttpServletRequest request, HttpServletResponse response) {
        logger.warn("[Springboot ExceptionHandler]", t);
        if (t instanceof OperationException)
        {
            return new ResponseEntity<>(new BtResponse(false, ((OperationException) t).getCode(),
                    ((OperationException) t).getData(), t.getMessage()), HttpStatus.OK);
        }
        return new ResponseEntity<>(new BtResponse(response.getStatus(), "Internal Server Error"), HttpStatus.resolve(response.getStatus()));
    }
}
