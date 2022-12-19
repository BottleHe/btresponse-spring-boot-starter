package work.bottle.plugin.exception.server;

import work.bottle.plugin.exception.ServerException;

public class HttpNotImplementedException extends ServerException {

    public static final HttpNotImplementedException Default
            = new HttpNotImplementedException();

    public HttpNotImplementedException() {
        super(501, "Not Implemented");
    }
}
