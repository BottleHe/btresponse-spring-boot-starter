package work.bottle.plugin.exception.server;

import work.bottle.plugin.exception.ServerException;

public class HttpUnprocessableEntityException extends ServerException {

    public static final HttpUnprocessableEntityException Default
            = new HttpUnprocessableEntityException();

    public HttpUnprocessableEntityException() {
        super(422, "Unprocessable Entity");
    }
}
