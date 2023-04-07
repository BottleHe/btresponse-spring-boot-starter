/**
* 请求在资源未准备充足的情况下到来, 服务端无法正常处理的情况
**/
package work.bottle.plugin.exception.global.server;

import work.bottle.plugin.exception.GlobalException;
import work.bottle.plugin.exception.global.GlobalError;

public final class UnavailableException extends GlobalException {

	public static final UnavailableException Default = new UnavailableException();

	public UnavailableException(String message) {
        super(503, message);
    }

    public UnavailableException() {
        super(503, "服务不可用，过载保护");
    }

    public UnavailableException(String message, Object data) {
        super(503, message, data);
    }

    public UnavailableException(String message, Object data, Throwable t) {
        super(503, message, data, t);
    }
}
