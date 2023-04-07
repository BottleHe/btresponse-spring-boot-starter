/**
* 服务器处理业务时, 应具有一个前置状态, 但状态检查不通过的情况.
**/
package work.bottle.plugin.exception.global.client;

import work.bottle.plugin.exception.GlobalException;
import work.bottle.plugin.exception.global.GlobalError;

public final class ConflictException extends GlobalException {

	public static final ConflictException Default = new ConflictException();

	public ConflictException(String message) {
        super(409, message);
    }

    public ConflictException() {
        super(409, "状态冲突");
    }

    public ConflictException(String message, Object data) {
        super(409, message, data);
    }

    public ConflictException(String message, Object data, Throwable t) {
        super(409, message, data, t);
    }
}
