package work.bottle.plugin.exception.server;

import work.bottle.plugin.exception.ServerException;

public class HttpTooManyRequestsException extends ServerException {

    public static final HttpTooManyRequestsException Default
            = new HttpTooManyRequestsException();

    public HttpTooManyRequestsException() {
        super(429, "Too Many Requests");
    }
}
