package work.bottle.plugin.exception.server;

import work.bottle.plugin.exception.ServerException;

public class HttpUnsupportedMediaTypeException extends ServerException {

    public static final HttpUnsupportedMediaTypeException Default
            = new HttpUnsupportedMediaTypeException();

    public HttpUnsupportedMediaTypeException() {
        super(415, "Unsupported Media Type");
    }
}
