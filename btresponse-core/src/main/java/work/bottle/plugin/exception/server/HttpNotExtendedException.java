package work.bottle.plugin.exception.server;

import work.bottle.plugin.exception.ServerException;

public class HttpNotExtendedException extends ServerException {

    public static final HttpNotExtendedException Default
            = new HttpNotExtendedException();

    public HttpNotExtendedException() {
        super(510, "Not Extended");
    }
}
