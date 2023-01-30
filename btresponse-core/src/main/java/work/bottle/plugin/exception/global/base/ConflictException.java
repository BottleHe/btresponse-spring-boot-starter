/**
* 服务器处理业务时, 应具有一个前置状态, 但状态检查不通过的情况.
**/
package work.bottle.plugin.exception.global.base;

import work.bottle.plugin.exception.GlobalException;

public final class ConflictException extends GlobalException {

    public static final ConflictException Default = new ConflictException();

    public ConflictException() {
        super(409, "状态冲突");
    }
}
