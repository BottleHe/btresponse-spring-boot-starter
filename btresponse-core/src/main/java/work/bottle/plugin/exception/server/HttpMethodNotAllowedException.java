package work.bottle.plugin.exception.server;

import work.bottle.plugin.exception.ServerException;

public class HttpMethodNotAllowedException extends ServerException {

    public static final HttpMethodNotAllowedException Default
            = new HttpMethodNotAllowedException();

    public HttpMethodNotAllowedException() {
        super(405, "Method Not Allowed");
    }
}
