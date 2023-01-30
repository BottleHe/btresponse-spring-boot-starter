/**
* 一般是有特定的参数不被支持, 比如支付下单接口的第三方支付方式异常. 可根据上下文判断是什么不被支持.
**/
package work.bottle.plugin.exception.global.base;

import work.bottle.plugin.exception.GlobalException;

public final class UnsupportedException extends GlobalException {

    public static final UnsupportedException Default = new UnsupportedException();

    public UnsupportedException() {
        super(415, "不支持的请求内容");
    }
}
