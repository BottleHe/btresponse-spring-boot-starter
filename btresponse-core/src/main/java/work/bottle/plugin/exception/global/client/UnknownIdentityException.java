/**
* 未知的身份, 一般存在于登录接口上, 表示用户不存在或未注册.
**/
package work.bottle.plugin.exception.global.client;

import work.bottle.plugin.exception.GlobalException;

public final class UnknownIdentityException extends GlobalException {

    public static final UnknownIdentityException Default = (UnknownIdentityException) GlobalError.getInstance().buildDefaultByCode(461);

	public UnknownIdentityException(String message) {
        super(461, message);
    }

    public UnknownIdentityException() {
        super(461, "身份未知");
    }

    public UnknownIdentityException(String message, Object data) {
        super(461, message, data);
    }

    public UnknownIdentityException(String message, Object data, Throwable t) {
        super(461, message, data, t);
    }
}
