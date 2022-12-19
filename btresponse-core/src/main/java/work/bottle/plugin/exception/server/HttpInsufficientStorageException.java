package work.bottle.plugin.exception.server;

import work.bottle.plugin.exception.ServerException;

public class HttpInsufficientStorageException extends ServerException {

    public static final HttpInsufficientStorageException Default
            = new HttpInsufficientStorageException();

    public HttpInsufficientStorageException() {
        super(507, "Insufficient Storage");
    }
}
