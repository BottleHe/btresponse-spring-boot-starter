package work.bottle.plugin.exception.server;

import work.bottle.plugin.exception.ServerException;

public class HttpFailedDependencyException extends ServerException {

    public static final HttpFailedDependencyException Default
            = new HttpFailedDependencyException();

    public HttpFailedDependencyException() {
        super(424, "Failed Dependency");
    }
}
