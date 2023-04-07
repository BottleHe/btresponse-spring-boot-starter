/**
* 表示请求的内容已过期(一般是指时间过晚)的异常 (非HTTP定义)
**/
package work.bottle.plugin.exception.global.client;

import work.bottle.plugin.exception.GlobalException;
import work.bottle.plugin.exception.global.GlobalError;

public final class ExpiredException extends GlobalException {

	public static final ExpiredException Default = new ExpiredException();

	public ExpiredException(String message) {
        super(460, message);
    }

    public ExpiredException() {
        super(460, "已过期异常");
    }

    public ExpiredException(String message, Object data) {
        super(460, message, data);
    }

    public ExpiredException(String message, Object data, Throwable t) {
        super(460, message, data, t);
    }
}
