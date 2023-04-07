/**
* 一般是指客户端上下文异常, 无效的上下文, 消息帧, 欺骗性上下文等
**/
package work.bottle.plugin.exception.global.client;

import work.bottle.plugin.exception.GlobalException;
import work.bottle.plugin.exception.global.GlobalError;

public final class ContextException extends GlobalException {

    public static final ContextException Default = (ContextException) GlobalError.getInstance().buildDefaultByCode(400);

	public ContextException(String message) {
        super(400, message);
    }

    public ContextException() {
        super(400, "上下文错误");
    }

    public ContextException(String message, Object data) {
        super(400, message, data);
    }

    public ContextException(String message, Object data, Throwable t) {
        super(400, message, data, t);
    }
}
