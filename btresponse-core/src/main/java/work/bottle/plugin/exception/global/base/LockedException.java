/**
* 访问的资源被锁定
**/
package work.bottle.plugin.exception.global.base;

import work.bottle.plugin.exception.GlobalException;

public final class LockedException extends GlobalException {

    public static final LockedException Default = new LockedException();

    public LockedException() {
        super(423, "已锁定异常");
    }
}
