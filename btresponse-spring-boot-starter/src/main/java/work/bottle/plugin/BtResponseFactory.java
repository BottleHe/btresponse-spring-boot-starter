package work.bottle.plugin;

import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.util.MultiValueMap;
import work.bottle.plugin.model.BtResponse;

public class BtResponseFactory implements StandardResponseFactory<BtResponse> {

    @Override
    public boolean isInstance(Object o) {
        return o instanceof BtResponse;
    }

    @Override
    public BtResponse produceResponse(int code, String message, Object data) {
        return produceResponse(0 == code ? true : false, code, message, data);
    }

    @Override
    public BtResponse produceResponse(boolean success, int code, String message, Object data) {
        return new BtResponse(success, code, data, message);
    }

    @Override
    public ResponseEntity<BtResponse> produceResponseEntity(boolean success, int code, String message, Object data, int httpStatus, @Nullable MultiValueMap<String, String> headers) {
        return new ResponseEntity<>(produceResponse(success, code, message, data), headers, httpStatus);
    }
}
