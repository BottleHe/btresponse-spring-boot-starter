/**
* 资源不足时触发
**/
package work.bottle.plugin.exception.global.server;

import work.bottle.plugin.exception.GlobalException;

public final class InsufficientException extends GlobalException {

    public static final InsufficientException Default = (InsufficientException) GlobalError.getInstance().buildDefaultByCode(561);

	public InsufficientException(String message) {
        super(561, message);
    }

    public InsufficientException() {
        super(561, "缺少可用资源");
    }

    public InsufficientException(String message, Object data) {
        super(561, message, data);
    }

    public InsufficientException(String message, Object data, Throwable t) {
        super(561, message, data, t);
    }
}
