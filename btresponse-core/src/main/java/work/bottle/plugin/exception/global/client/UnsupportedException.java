/**
* 一般是有特定的参数不被支持, 比如支付下单接口的第三方支付方式异常. 可根据上下文判断是什么不被支持.
**/
package work.bottle.plugin.exception.global.client;

import work.bottle.plugin.exception.GlobalException;
import work.bottle.plugin.exception.global.GlobalError;

public final class UnsupportedException extends GlobalException {

	public static final UnsupportedException Default = new UnsupportedException();

	public UnsupportedException(String message) {
        super(415, message);
    }

    public UnsupportedException() {
        super(415, "不支持的请求内容");
    }

    public UnsupportedException(String message, Object data) {
        super(415, message, data);
    }

    public UnsupportedException(String message, Object data, Throwable t) {
        super(415, message, data, t);
    }
}
