package work.bottle.plugin.exception.server;

import work.bottle.plugin.exception.ServerException;

public class HttpMethodFailureException extends ServerException {

    public static final HttpMethodFailureException Default
            = new HttpMethodFailureException();

    public HttpMethodFailureException() {
        super(420, "Method Failure");
    }
}
