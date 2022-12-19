package work.bottle.plugin.exception.server;

import work.bottle.plugin.exception.ServerException;

public class HttpNotAcceptableException extends ServerException {

    public static final HttpNotAcceptableException Default
            = new HttpNotAcceptableException();

    public HttpNotAcceptableException() {
        super(406, "Not Acceptable");
    }
}
