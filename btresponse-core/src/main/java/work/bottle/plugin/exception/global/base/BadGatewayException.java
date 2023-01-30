/**
* 存在第三方外部调用的情况下, 外部链路出现未知的错误的情况
**/
package work.bottle.plugin.exception.global.base;

import work.bottle.plugin.exception.GlobalException;

public final class BadGatewayException extends GlobalException {

    public static final BadGatewayException Default = new BadGatewayException();

    public BadGatewayException() {
        super(502, "服务器后端链路响应错误");
    }
}
