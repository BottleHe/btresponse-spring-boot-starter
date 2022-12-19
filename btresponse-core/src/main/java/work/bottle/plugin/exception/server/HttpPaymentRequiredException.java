package work.bottle.plugin.exception.server;

import work.bottle.plugin.exception.ServerException;

public class HttpPaymentRequiredException extends ServerException {

    public static final HttpPaymentRequiredException Default
            = new HttpPaymentRequiredException();

    public HttpPaymentRequiredException() {
        super(402, "Payment Required");
    }
}
