/**
* 一般是需要授权的接口, 用户访问时未被授权
**/
package work.bottle.plugin.exception.global.client;

import work.bottle.plugin.exception.GlobalException;
import work.bottle.plugin.exception.global.GlobalError;

public final class ForbiddenException extends GlobalException {

	public static final ForbiddenException Default = new ForbiddenException();

	public ForbiddenException(String message) {
        super(403, message);
    }

    public ForbiddenException() {
        super(403, "访问被拒绝, 缺失权限访问");
    }

    public ForbiddenException(String message, Object data) {
        super(403, message, data);
    }

    public ForbiddenException(String message, Object data, Throwable t) {
        super(403, message, data, t);
    }
}
