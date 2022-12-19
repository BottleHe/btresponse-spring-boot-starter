package work.bottle.plugin.exception.server;

import work.bottle.plugin.exception.ServerException;

public class HttpNotFoundException extends ServerException {

    public static final HttpNotFoundException Default
            = new HttpNotFoundException();

    public HttpNotFoundException() {
        super(404, "Not Found");
    }
}
