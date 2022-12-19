package work.bottle.plugin.exception.server;

import work.bottle.plugin.exception.ServerException;

public class HttpBandwidthLimitExceededException extends ServerException {

    public static final HttpBandwidthLimitExceededException Default
            = new HttpBandwidthLimitExceededException();

    public HttpBandwidthLimitExceededException() {
        super(509, "Bandwidth Limit Exceeded");
    }
}
