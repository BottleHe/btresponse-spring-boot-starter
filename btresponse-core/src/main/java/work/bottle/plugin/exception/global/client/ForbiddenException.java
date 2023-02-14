/**
* 一般是需要授权的接口, 用户访问时未被授权
**/
package work.bottle.plugin.exception.global.client;

import work.bottle.plugin.exception.GlobalException;

public final class ForbiddenException extends GlobalException {

    public static final ForbiddenException Default = new ForbiddenException();

	public ForbiddenException(String message) {
        super(403, message);
    }

    public ForbiddenException() {
        super(403, "访问被拒绝, 缺失权限访问");
    }
}
