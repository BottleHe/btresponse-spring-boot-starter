package work.bottle.plugin.exception.server;

import work.bottle.plugin.exception.ServerException;

public class HttpIAmATeapotException extends ServerException {

    public static final HttpIAmATeapotException Default
            = new HttpIAmATeapotException();

    public HttpIAmATeapotException() {
        super(418, "I'm a teapot");
    }
}
