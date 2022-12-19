package work.bottle.plugin.exception.server;

import work.bottle.plugin.exception.ServerException;

public class HttpUnauthorizedException extends ServerException {

    public static final HttpUnauthorizedException Default
            = new HttpUnauthorizedException();

    public HttpUnauthorizedException() {
        super(401, "Unauthorized");
    }
}
