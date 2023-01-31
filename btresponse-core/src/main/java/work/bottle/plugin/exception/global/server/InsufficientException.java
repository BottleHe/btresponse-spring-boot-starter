/**
* 资源不足时触发
**/
package work.bottle.plugin.exception.global.server;

import work.bottle.plugin.exception.GlobalException;

public final class InsufficientException extends GlobalException {

    public static final InsufficientException Default = new InsufficientException();

    public InsufficientException() {
        super(561, "缺少可用资源");
    }
}
