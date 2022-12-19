package work.bottle.plugin.exception.server;

import work.bottle.plugin.exception.ServerException;

public class HttpRequestTimeoutException extends ServerException {

    public static final HttpRequestTimeoutException Default
            = new HttpRequestTimeoutException();

    public HttpRequestTimeoutException() {
        super(408, "Request Timeout");
    }
}
