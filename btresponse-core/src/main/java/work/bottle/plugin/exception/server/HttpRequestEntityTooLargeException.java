package work.bottle.plugin.exception.server;

import work.bottle.plugin.exception.ServerException;

public class HttpRequestEntityTooLargeException extends ServerException {

    public static final HttpRequestEntityTooLargeException Default
            = new HttpRequestEntityTooLargeException();

    public HttpRequestEntityTooLargeException() {
        super(413, "Request Entity Too Large");
    }
}
