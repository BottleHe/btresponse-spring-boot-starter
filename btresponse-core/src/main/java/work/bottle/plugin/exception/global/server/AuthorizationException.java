/**
* 授权异常, 表示授权失败. 
**/
package work.bottle.plugin.exception.global.server;

import work.bottle.plugin.exception.GlobalException;
import work.bottle.plugin.exception.global.GlobalError;

public final class AuthorizationException extends GlobalException {

	public static final AuthorizationException Default = new AuthorizationException();

	public AuthorizationException(String message) {
        super(563, message);
    }

    public AuthorizationException() {
        super(563, "授权异常");
    }

    public AuthorizationException(String message, Object data) {
        super(563, message, data);
    }

    public AuthorizationException(String message, Object data, Throwable t) {
        super(563, message, data, t);
    }
}
