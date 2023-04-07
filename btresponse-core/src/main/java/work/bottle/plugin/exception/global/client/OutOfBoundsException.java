/**
* 请求上下文超出可容纳的范围的情况.
**/
package work.bottle.plugin.exception.global.client;

import work.bottle.plugin.exception.GlobalException;

public final class OutOfBoundsException extends GlobalException {

    public static final OutOfBoundsException Default = (OutOfBoundsException) GlobalError.getInstance().buildDefaultByCode(416);

	public OutOfBoundsException(String message) {
        super(416, message);
    }

    public OutOfBoundsException() {
        super(416, "请求越界");
    }

    public OutOfBoundsException(String message, Object data) {
        super(416, message, data);
    }

    public OutOfBoundsException(String message, Object data, Throwable t) {
        super(416, message, data, t);
    }
}
