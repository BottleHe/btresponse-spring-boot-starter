package work.bottle.plugin.exception.server;

import work.bottle.plugin.exception.ServerException;

public class HttpHttpVersionNotSupportedException extends ServerException {

    public static final HttpHttpVersionNotSupportedException Default
            = new HttpHttpVersionNotSupportedException();

    public HttpHttpVersionNotSupportedException() {
        super(505, "HTTP Version not supported");
    }
}
