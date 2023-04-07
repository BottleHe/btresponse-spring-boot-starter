/**
* 表示请求的内容还未能使用(一般是指时间过早)的异常
**/
package work.bottle.plugin.exception.global.client;

import work.bottle.plugin.exception.GlobalException;
import work.bottle.plugin.exception.global.GlobalError;

public final class PrematureException extends GlobalException {

    public static final PrematureException Default = (PrematureException) GlobalError.getInstance().buildDefaultByCode(425);

	public PrematureException(String message) {
        super(425, message);
    }

    public PrematureException() {
        super(425, "过早的异常");
    }

    public PrematureException(String message, Object data) {
        super(425, message, data);
    }

    public PrematureException(String message, Object data, Throwable t) {
        super(425, message, data, t);
    }
}
