/**
* 操作处理超时
**/
package work.bottle.plugin.exception.global.client;

import work.bottle.plugin.exception.GlobalException;

public final class TimeoutException extends GlobalException {

    public static final TimeoutException Default = new TimeoutException();

    public TimeoutException() {
        super(408, "处理超时");
    }
}
