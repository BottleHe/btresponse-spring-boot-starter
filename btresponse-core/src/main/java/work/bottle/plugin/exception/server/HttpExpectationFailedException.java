package work.bottle.plugin.exception.server;

import work.bottle.plugin.exception.ServerException;

public class HttpExpectationFailedException extends ServerException {

    public static final HttpExpectationFailedException Default
            = new HttpExpectationFailedException();

    public HttpExpectationFailedException() {
        super(417, "Expectation Failed");
    }
}
