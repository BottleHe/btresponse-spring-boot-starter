package work.bottle.plugin.exception.server;

import work.bottle.plugin.exception.ServerException;

public class HttpInsufficientSpaceOnResourceException extends ServerException {

    public static final HttpInsufficientSpaceOnResourceException Default
            = new HttpInsufficientSpaceOnResourceException();

    public HttpInsufficientSpaceOnResourceException() {
        super(419, "Insufficient Space On Resource");
    }
}
