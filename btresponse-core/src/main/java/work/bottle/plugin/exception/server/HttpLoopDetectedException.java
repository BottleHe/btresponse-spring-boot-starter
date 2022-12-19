package work.bottle.plugin.exception.server;

import work.bottle.plugin.exception.ServerException;

public class HttpLoopDetectedException extends ServerException {

    public static final HttpLoopDetectedException Default
            = new HttpLoopDetectedException();

    public HttpLoopDetectedException() {
        super(508, "Loop Detected");
    }
}
