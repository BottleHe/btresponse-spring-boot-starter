/**
* 访问的资源被锁定
**/
package work.bottle.plugin.exception.global.client;

import work.bottle.plugin.exception.GlobalException;
import work.bottle.plugin.exception.global.GlobalError;

public final class LockedException extends GlobalException {

    public static final LockedException Default = (LockedException) GlobalError.getInstance().buildDefaultByCode(423);

	public LockedException(String message) {
        super(423, message);
    }

    public LockedException() {
        super(423, "已锁定异常");
    }

    public LockedException(String message, Object data) {
        super(423, message, data);
    }

    public LockedException(String message, Object data, Throwable t) {
        super(423, message, data, t);
    }
}
