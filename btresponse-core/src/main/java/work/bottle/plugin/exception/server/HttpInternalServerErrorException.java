package work.bottle.plugin.exception.server;

import work.bottle.plugin.exception.ServerException;

public class HttpInternalServerErrorException extends ServerException {

    public static final HttpInternalServerErrorException Default
            = new HttpInternalServerErrorException();

    public HttpInternalServerErrorException() {
        super(500, "Internal Server Error");
    }
}
