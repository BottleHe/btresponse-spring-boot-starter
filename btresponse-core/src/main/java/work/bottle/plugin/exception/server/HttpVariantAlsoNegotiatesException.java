package work.bottle.plugin.exception.server;

import work.bottle.plugin.exception.ServerException;

public class HttpVariantAlsoNegotiatesException extends ServerException {

    public static final HttpVariantAlsoNegotiatesException Default
            = new HttpVariantAlsoNegotiatesException();

    public HttpVariantAlsoNegotiatesException() {
        super(506, "Variant Also Negotiates");
    }
}
