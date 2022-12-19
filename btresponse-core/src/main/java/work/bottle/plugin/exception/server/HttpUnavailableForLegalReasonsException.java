package work.bottle.plugin.exception.server;

import work.bottle.plugin.exception.ServerException;

public class HttpUnavailableForLegalReasonsException extends ServerException {

    public static final HttpUnavailableForLegalReasonsException Default
            = new HttpUnavailableForLegalReasonsException();

    public HttpUnavailableForLegalReasonsException() {
        super(451, "Unavailable For Legal Reasons");
    }
}
