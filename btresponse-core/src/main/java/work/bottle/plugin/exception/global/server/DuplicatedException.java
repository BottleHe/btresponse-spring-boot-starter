/**
* 重复调用时触发, 和Uniqueness类似
**/
package work.bottle.plugin.exception.global.server;

import work.bottle.plugin.exception.GlobalException;
import work.bottle.plugin.exception.global.GlobalError;

public final class DuplicatedException extends GlobalException {

    public static final DuplicatedException Default = (DuplicatedException) GlobalError.getInstance().buildDefaultByCode(562);

	public DuplicatedException(String message) {
        super(562, message);
    }

    public DuplicatedException() {
        super(562, "重复调用异常");
    }

    public DuplicatedException(String message, Object data) {
        super(562, message, data);
    }

    public DuplicatedException(String message, Object data, Throwable t) {
        super(562, message, data, t);
    }
}
