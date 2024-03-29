/**
* 请求被服务器拒绝, 原因未知
**/
package work.bottle.plugin.exception.global.client;

import work.bottle.plugin.exception.GlobalException;
import work.bottle.plugin.exception.global.GlobalError;

public final class NegativeException extends GlobalException {

	public static final NegativeException Default = new NegativeException();

	public NegativeException(String message) {
        super(418, message);
    }

    public NegativeException() {
        super(418, "服务器拒绝");
    }

    public NegativeException(String message, Object data) {
        super(418, message, data);
    }

    public NegativeException(String message, Object data, Throwable t) {
        super(418, message, data, t);
    }
}
