/**
* 服务器无法接受客户端的访问
**/
package work.bottle.plugin.exception.global.client;

import work.bottle.plugin.exception.GlobalException;
import work.bottle.plugin.exception.global.GlobalError;

public final class NotAcceptableException extends GlobalException {

	public static final NotAcceptableException Default = new NotAcceptableException();

	public NotAcceptableException(String message) {
        super(406, message);
    }

    public NotAcceptableException() {
        super(406, "不接受的访问");
    }

    public NotAcceptableException(String message, Object data) {
        super(406, message, data);
    }

    public NotAcceptableException(String message, Object data, Throwable t) {
        super(406, message, data, t);
    }
}
