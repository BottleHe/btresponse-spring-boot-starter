package work.bottle.plugin.exception.server;

import work.bottle.plugin.exception.ServerException;

public class HttpPreconditionRequiredException extends ServerException {

    public static final HttpPreconditionRequiredException Default
            = new HttpPreconditionRequiredException();

    public HttpPreconditionRequiredException() {
        super(428, "Precondition Required");
    }
}
