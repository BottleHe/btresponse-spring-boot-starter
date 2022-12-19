package work.bottle.plugin.exception.server;

import work.bottle.plugin.exception.ServerException;

public class HttpProxyAuthenticationRequiredException extends ServerException {

    public static final HttpProxyAuthenticationRequiredException Default
            = new HttpProxyAuthenticationRequiredException();

    public HttpProxyAuthenticationRequiredException() {
        super(407, "Proxy Authentication Required");
    }
}
