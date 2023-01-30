/**
* 授权异常, 表示授权失败. 
**/
package work.bottle.plugin.exception.global.base;

import work.bottle.plugin.exception.GlobalException;

public final class AuthorizationException extends GlobalException {

    public static final AuthorizationException Default = new AuthorizationException();

    public AuthorizationException() {
        super(461, "授权异常");
    }
}
