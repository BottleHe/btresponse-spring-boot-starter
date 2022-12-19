package work.bottle.plugin.exception.server;

import work.bottle.plugin.exception.ServerException;

public class HttpLengthRequiredException extends ServerException {

    public static final HttpLengthRequiredException Default
            = new HttpLengthRequiredException();

    public HttpLengthRequiredException() {
        super(411, "Length Required");
    }
}
