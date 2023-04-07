/**
* 请求内容超出服务能够处理的上限
**/
package work.bottle.plugin.exception.global.client;

import work.bottle.plugin.exception.GlobalException;
import work.bottle.plugin.exception.global.GlobalError;

public final class TooLargeException extends GlobalException {

    public static final TooLargeException Default = (TooLargeException) GlobalError.getInstance().buildDefaultByCode(413);

	public TooLargeException(String message) {
        super(413, message);
    }

    public TooLargeException() {
        super(413, "请求过大");
    }

    public TooLargeException(String message, Object data) {
        super(413, message, data);
    }

    public TooLargeException(String message, Object data, Throwable t) {
        super(413, message, data, t);
    }
}
