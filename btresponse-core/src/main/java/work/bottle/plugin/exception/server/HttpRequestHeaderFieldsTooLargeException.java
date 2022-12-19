package work.bottle.plugin.exception.server;

import work.bottle.plugin.exception.ServerException;

public class HttpRequestHeaderFieldsTooLargeException extends ServerException {

    public static final HttpRequestHeaderFieldsTooLargeException Default
            = new HttpRequestHeaderFieldsTooLargeException();

    public HttpRequestHeaderFieldsTooLargeException() {
        super(431, "Request Header Fields Too Large");
    }
}
