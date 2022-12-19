package work.bottle.plugin.exception.server;

import work.bottle.plugin.exception.ServerException;

public class HttpDestinationLockedException extends ServerException {

    public static final HttpDestinationLockedException Default
            = new HttpDestinationLockedException();

    public HttpDestinationLockedException() {
        super(421, "Destination Locked");
    }
}
