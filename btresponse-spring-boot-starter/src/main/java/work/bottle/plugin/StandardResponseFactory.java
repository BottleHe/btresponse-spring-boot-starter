package work.bottle.plugin;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.util.MultiValueMap;
import work.bottle.plugin.exception.GlobalException;
import work.bottle.plugin.exception.OperationException;

public interface StandardResponseFactory<T> {
    static final String EMPTY_STR = "";

    boolean isInstance(Object o);

    default T produceDefaultResponse() {
        return produceResponse(true, 0, EMPTY_STR, null);
    }

    default T produceSuccessResponse(Object data) {
        return produceResponse(true, 0, EMPTY_STR, data);
    }

    default T produceErrorResponse(OperationException e) {
        return produceResponse(false, e.getCode(), e.getMessage(), e.getData());
    }

    default T produceErrorResponse(GlobalException e) {
        return produceResponse(false, e.getCode(), e.getMessage(), e.getData());
    }

    T produceResponse(int code, String message, Object data);

    T produceResponse(boolean success, int code, String message, Object data);

    default ResponseEntity<T> produceErrorResponseEntity(OperationException e) {
        return produceResponseEntity(false, e.getCode(), e.getMessage(),
                e.getData(), HttpStatus.OK.value(), null);
    }

    default ResponseEntity<T> produceErrorResponseEntity(Throwable t, int httpStatus) {
        return produceResponseEntity(false, httpStatus, t.getMessage(), null, httpStatus, null);
    }

    ResponseEntity<T> produceResponseEntity(boolean success, int code, String message,
                                            Object data, int httpStatus, @Nullable MultiValueMap<String, String> headers);
}
