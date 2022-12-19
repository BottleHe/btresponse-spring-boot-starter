package work.bottle.plugin.exception.server;

import work.bottle.plugin.exception.ServerException;

public class HttpGoneException extends ServerException {

    public static final HttpGoneException Default
            = new HttpGoneException();

    public HttpGoneException() {
        super(410, "Gone");
    }
}
