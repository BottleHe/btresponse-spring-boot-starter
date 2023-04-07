/**
* 操作处理超时
**/
package work.bottle.plugin.exception.global.client;

import work.bottle.plugin.exception.GlobalException;

public final class TimeoutException extends GlobalException {

    public static final TimeoutException Default = (TimeoutException) GlobalError.getInstance().buildDefaultByCode(408);

	public TimeoutException(String message) {
        super(408, message);
    }

    public TimeoutException() {
        super(408, "处理超时");
    }

    public TimeoutException(String message, Object data) {
        super(408, message, data);
    }

    public TimeoutException(String message, Object data, Throwable t) {
        super(408, message, data, t);
    }
}
