package work.bottle.plugin.exception.server;

import work.bottle.plugin.exception.ServerException;

public class HttpRequestedRangeNotSatisfiableException extends ServerException {

    public static final HttpRequestedRangeNotSatisfiableException Default
            = new HttpRequestedRangeNotSatisfiableException();

    public HttpRequestedRangeNotSatisfiableException() {
        super(416, "Requested range not satisfiable");
    }
}
