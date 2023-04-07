/**
* 存在第三方外部调用的情况下, 外部链路出现未知的错误的情况
**/
package work.bottle.plugin.exception.global.server;

import work.bottle.plugin.exception.GlobalException;
import work.bottle.plugin.exception.global.GlobalError;

public final class BadGatewayException extends GlobalException {

	public static final BadGatewayException Default = new BadGatewayException();

	public BadGatewayException(String message) {
        super(502, message);
    }

    public BadGatewayException() {
        super(502, "服务下线，暂时不可用");
    }

    public BadGatewayException(String message, Object data) {
        super(502, message, data);
    }

    public BadGatewayException(String message, Object data, Throwable t) {
        super(502, message, data, t);
    }
}
