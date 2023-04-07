/**
* 对应Internal server error
**/
package work.bottle.plugin.exception.global.server;

import work.bottle.plugin.exception.GlobalException;
import work.bottle.plugin.exception.global.GlobalError;

public final class UnknownException extends GlobalException {

    public static final UnknownException Default = (UnknownException) GlobalError.getInstance().buildDefaultByCode(500);

	public UnknownException(String message) {
        super(500, message);
    }

    public UnknownException() {
        super(500, "服务处理失败");
    }

    public UnknownException(String message, Object data) {
        super(500, message, data);
    }

    public UnknownException(String message, Object data, Throwable t) {
        super(500, message, data, t);
    }
}
