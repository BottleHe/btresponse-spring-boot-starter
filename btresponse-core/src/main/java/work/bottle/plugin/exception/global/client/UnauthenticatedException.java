/**
* 一般是需要身份认证的接口, 用户访问时未登录触发. 
**/
package work.bottle.plugin.exception.global.client;

import work.bottle.plugin.exception.GlobalException;
import work.bottle.plugin.exception.global.GlobalError;

public final class UnauthenticatedException extends GlobalException {

    public static final UnauthenticatedException Default = (UnauthenticatedException) GlobalError.getInstance().buildDefaultByCode(401);

	public UnauthenticatedException(String message) {
        super(401, message);
    }

    public UnauthenticatedException() {
        super(401, "身份未认证异常, 请登录后重试");
    }

    public UnauthenticatedException(String message, Object data) {
        super(401, message, data);
    }

    public UnauthenticatedException(String message, Object data, Throwable t) {
        super(401, message, data, t);
    }
}
