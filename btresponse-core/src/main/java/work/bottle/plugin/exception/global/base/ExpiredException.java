/**
* 表示请求的内容已过期(一般是指时间过晚)的异常 (非HTTP定义)
**/
package work.bottle.plugin.exception.global.base;

import work.bottle.plugin.exception.GlobalException;

public final class ExpiredException extends GlobalException {

    public static final ExpiredException Default = new ExpiredException();

    public ExpiredException() {
        super(460, "已过期异常");
    }
}
