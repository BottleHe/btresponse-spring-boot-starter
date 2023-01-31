/**
* 未知的身份, 一般存在于登录接口上, 表示用户不存在或未注册.
**/
package work.bottle.plugin.exception.global.client;

import work.bottle.plugin.exception.GlobalException;

public final class UnknownIdentityException extends GlobalException {

    public static final UnknownIdentityException Default = new UnknownIdentityException();

    public UnknownIdentityException() {
        super(461, "身份未知");
    }
}
