package work.bottle.plugin;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import work.bottle.plugin.exception.OperationException;
import work.bottle.plugin.model.BtResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// @ControllerAdvice
public class BaseResponseBodyExceptionHandler {

    @ExceptionHandler(Throwable.class)
    @ResponseBody
    public BtResponse baseExceptionHandler(Throwable t, HttpServletRequest request, HttpServletResponse response) {
        // System.out.println("内部异常");
        if (t instanceof OperationException)
        {
            response.setStatus(200);
            return new BtResponse(false, ((OperationException) t).getCode(),
                    ((OperationException) t).getData(), t.getMessage());
        }
        response.setStatus(500);
        return new BtResponse(500, "Internal Server Error");
    }
}
