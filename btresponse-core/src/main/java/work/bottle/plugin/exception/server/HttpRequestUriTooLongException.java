package work.bottle.plugin.exception.server;

import work.bottle.plugin.exception.ServerException;

public class HttpRequestUriTooLongException extends ServerException {

    public static final HttpRequestUriTooLongException Default
            = new HttpRequestUriTooLongException();

    public HttpRequestUriTooLongException() {
        super(414, "Request-URI Too Long");
    }
}
