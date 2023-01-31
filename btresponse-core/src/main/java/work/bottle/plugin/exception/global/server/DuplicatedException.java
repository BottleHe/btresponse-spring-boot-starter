/**
* 重复调用时触发, 和Uniqueness类似
**/
package work.bottle.plugin.exception.global.server;

import work.bottle.plugin.exception.GlobalException;

public final class DuplicatedException extends GlobalException {

    public static final DuplicatedException Default = new DuplicatedException();

    public DuplicatedException() {
        super(562, "重复调用异常");
    }
}
