/**
* 授权异常, 表示授权失败. 
**/
package work.bottle.plugin.exception.global.server;

import work.bottle.plugin.exception.GlobalException;

public final class AuthorizationException extends GlobalException {

    public static final AuthorizationException Default = new AuthorizationException();

    public AuthorizationException() {
        super(563, "授权异常");
    }
}
