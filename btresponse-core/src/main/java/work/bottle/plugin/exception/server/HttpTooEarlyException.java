package work.bottle.plugin.exception.server;

import work.bottle.plugin.exception.ServerException;

public class HttpTooEarlyException extends ServerException {

    public static final HttpTooEarlyException Default
            = new HttpTooEarlyException();

    public HttpTooEarlyException() {
        super(425, "Too Early");
    }
}
