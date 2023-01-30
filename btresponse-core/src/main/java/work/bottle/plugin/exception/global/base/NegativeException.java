/**
* 请求被服务器拒绝, 原因未知
**/
package work.bottle.plugin.exception.global.base;

import work.bottle.plugin.exception.GlobalException;

public final class NegativeException extends GlobalException {

    public static final NegativeException Default = new NegativeException();

    public NegativeException() {
        super(418, "服务器拒绝");
    }
}
