package work.bottle.plugin.exception.server;

import work.bottle.plugin.exception.ServerException;

public class HttpServiceUnavailableException extends ServerException {

    public static final HttpServiceUnavailableException Default
            = new HttpServiceUnavailableException();

    public HttpServiceUnavailableException() {
        super(503, "Service Unavailable");
    }
}
