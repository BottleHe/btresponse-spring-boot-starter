package work.bottle.plugin.exception.server;

import work.bottle.plugin.exception.ServerException;

public class HttpForbiddenException extends ServerException {

    public static final HttpForbiddenException Default
            = new HttpForbiddenException();

    public HttpForbiddenException() {
        super(403, "Forbidden");
    }
}
