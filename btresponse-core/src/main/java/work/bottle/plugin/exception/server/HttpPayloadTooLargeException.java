package work.bottle.plugin.exception.server;

import work.bottle.plugin.exception.ServerException;

public class HttpPayloadTooLargeException extends ServerException {

    public static final HttpPayloadTooLargeException Default
            = new HttpPayloadTooLargeException();

    public HttpPayloadTooLargeException() {
        super(413, "Payload Too Large");
    }
}
