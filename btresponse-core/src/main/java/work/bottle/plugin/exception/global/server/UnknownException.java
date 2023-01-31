/**
* 对应Internal server error
**/
package work.bottle.plugin.exception.global.server;

import work.bottle.plugin.exception.GlobalException;

public final class UnknownException extends GlobalException {

    public static final UnknownException Default = new UnknownException();

    public UnknownException() {
        super(500, "服务处理失败");
    }
}
