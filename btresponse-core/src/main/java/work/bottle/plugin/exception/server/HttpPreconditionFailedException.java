package work.bottle.plugin.exception.server;

import work.bottle.plugin.exception.ServerException;

public class HttpPreconditionFailedException extends ServerException {

    public static final HttpPreconditionFailedException Default
            = new HttpPreconditionFailedException();

    public HttpPreconditionFailedException() {
        super(412, "Precondition Failed");
    }
}
