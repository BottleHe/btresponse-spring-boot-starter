/**
* 请求在资源未准备充足的情况下到来, 服务端无法正常处理的情况
**/
package work.bottle.plugin.exception.global.base;

import work.bottle.plugin.exception.GlobalException;

public final class UnavailableException extends GlobalException {

    public static final UnavailableException Default = new UnavailableException();

    public UnavailableException() {
        super(503, "服务器未准备好");
    }
}
