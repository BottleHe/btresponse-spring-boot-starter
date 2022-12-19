package work.bottle.plugin.exception.server;

import work.bottle.plugin.exception.ServerException;

public class HttpLockedException extends ServerException {

    public static final HttpLockedException Default
            = new HttpLockedException();

    public HttpLockedException() {
        super(423, "Locked");
    }
}
