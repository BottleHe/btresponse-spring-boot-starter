/**
* 一般是由于上报的数据格式有问题而触发
**/
package work.bottle.plugin.exception.global.base;

import work.bottle.plugin.exception.GlobalException;

public final class UnprocessableException extends GlobalException {

    public static final UnprocessableException Default = new UnprocessableException();

    public UnprocessableException() {
        super(422, "内容无法识别, 请确认格式");
    }
}
