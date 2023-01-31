/**
* 一般是需要身份认证的接口, 用户访问时未登录触发. 
**/
package work.bottle.plugin.exception.global.client;

import work.bottle.plugin.exception.GlobalException;

public final class UnauthenticatedException extends GlobalException {

    public static final UnauthenticatedException Default = new UnauthenticatedException();

    public UnauthenticatedException() {
        super(401, "身份未认证异常, 请登录后重试");
    }
}
