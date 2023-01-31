/**
* 表示请求的内容还未能使用(一般是指时间过早)的异常
**/
package work.bottle.plugin.exception.global.client;

import work.bottle.plugin.exception.GlobalException;

public final class PrematureException extends GlobalException {

    public static final PrematureException Default = new PrematureException();

    public PrematureException() {
        super(425, "过早的异常");
    }
}
