package work.bottle.plugin.exception.server;

import work.bottle.plugin.exception.ServerException;

public class HttpConflictException extends ServerException {

    public static final HttpConflictException Default
            = new HttpConflictException();

    public HttpConflictException() {
        super(409, "Conflict");
    }
}
