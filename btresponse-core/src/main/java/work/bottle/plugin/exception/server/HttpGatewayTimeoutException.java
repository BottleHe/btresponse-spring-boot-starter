package work.bottle.plugin.exception.server;

import work.bottle.plugin.exception.ServerException;

public class HttpGatewayTimeoutException extends ServerException {

    public static final HttpGatewayTimeoutException Default
            = new HttpGatewayTimeoutException();

    public HttpGatewayTimeoutException() {
        super(504, "Gateway Timeout");
    }
}
