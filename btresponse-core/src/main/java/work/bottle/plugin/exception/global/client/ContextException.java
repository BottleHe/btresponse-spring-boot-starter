/**
* 一般是指客户端上下文异常, 无效的上下文, 消息帧, 欺骗性上下文等
**/
package work.bottle.plugin.exception.global.client;

import work.bottle.plugin.exception.GlobalException;

public final class ContextException extends GlobalException {

    public static final ContextException Default = new ContextException();

    public ContextException() {
        super(400, "上下文错误");
    }
}
