package work.bottle.plugin.exception.server;

import work.bottle.plugin.exception.ServerException;

public class HttpUpgradeRequiredException extends ServerException {

    public static final HttpUpgradeRequiredException Default
            = new HttpUpgradeRequiredException();

    public HttpUpgradeRequiredException() {
        super(426, "Upgrade Required");
    }
}
