/**
* 请求上下文超出可容纳的范围的情况.
**/
package work.bottle.plugin.exception.global.base;

import work.bottle.plugin.exception.GlobalException;

public final class OutOfBoundsException extends GlobalException {

    public static final OutOfBoundsException Default = new OutOfBoundsException();

    public OutOfBoundsException() {
        super(416, "请求越界");
    }
}
