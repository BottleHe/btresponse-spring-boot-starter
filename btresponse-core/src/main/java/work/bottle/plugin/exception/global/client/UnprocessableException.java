/**
* 单纯程序处理失败. 大多是由于上报的数据格式有问题而触发
**/
package work.bottle.plugin.exception.global.client;

import work.bottle.plugin.exception.GlobalException;

public final class UnprocessableException extends GlobalException {

    public static final UnprocessableException Default = new UnprocessableException();

	public UnprocessableException(String message) {
        super(422, message);
    }

    public UnprocessableException() {
        super(422, "处理失败, 不能处理.");
    }
}
