package work.bottle.plugin.exception.server;

import work.bottle.plugin.exception.ServerException;

public class HttpBadRequestException extends ServerException {

    public static final HttpBadRequestException Default
            = new HttpBadRequestException();

    public HttpBadRequestException() {
        super(400, "Bad Request");
    }
}
