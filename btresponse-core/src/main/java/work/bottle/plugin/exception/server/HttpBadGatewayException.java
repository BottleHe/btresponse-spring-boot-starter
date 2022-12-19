package work.bottle.plugin.exception.server;

import work.bottle.plugin.exception.ServerException;

public class HttpBadGatewayException extends ServerException {

    public static final HttpBadGatewayException Default
            = new HttpBadGatewayException();

    public HttpBadGatewayException() {
        super(502, "Bad Gateway");
    }
}
