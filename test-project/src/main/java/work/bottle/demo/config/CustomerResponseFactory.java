package work.bottle.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import work.bottle.demo.model.CustomResponse;
import work.bottle.plugin.StandardResponseFactory;

// @Configuration
public class CustomerResponseFactory implements StandardResponseFactory<CustomResponse> {
    @Override
    public boolean isInstance(Object o) {
        return o instanceof CustomResponse;
    }

    @Override
    public CustomResponse produceResponse(int code, String message, Object data) {
        return new CustomResponse(code, message, data);
    }

    @Override
    public CustomResponse produceResponse(boolean success, int code, String message, Object data) {
        return new CustomResponse(code, message, data);
    }

    @Override
    public ResponseEntity<CustomResponse> produceResponseEntity(boolean success, int code, String message, Object data, HttpStatus httpStatus, MultiValueMap<String, String> headers) {
        return new ResponseEntity<CustomResponse>(new CustomResponse(code, message, data), headers, httpStatus);
    }
}
