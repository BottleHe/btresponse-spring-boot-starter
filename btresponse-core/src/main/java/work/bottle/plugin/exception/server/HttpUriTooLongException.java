package work.bottle.plugin.exception.server;

import work.bottle.plugin.exception.ServerException;

public class HttpUriTooLongException extends ServerException {

    public static final HttpUriTooLongException Default
            = new HttpUriTooLongException();

    public HttpUriTooLongException() {
        super(414, "URI Too Long");
    }
}
