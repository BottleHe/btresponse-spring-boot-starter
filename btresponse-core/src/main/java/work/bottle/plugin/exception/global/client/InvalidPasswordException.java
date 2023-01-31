/**
* 需要身份认证时(比如登录接口), 口令不正确触发的异常
**/
package work.bottle.plugin.exception.global.client;

import work.bottle.plugin.exception.GlobalException;

public final class InvalidPasswordException extends GlobalException {

    public static final InvalidPasswordException Default = new InvalidPasswordException();

    public InvalidPasswordException() {
        super(462, "无效的口令");
    }
}
