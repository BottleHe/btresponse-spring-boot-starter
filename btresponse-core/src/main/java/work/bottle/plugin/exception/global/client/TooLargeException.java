/**
* 请求内容超出服务能够处理的上限
**/
package work.bottle.plugin.exception.global.client;

import work.bottle.plugin.exception.GlobalException;

public final class TooLargeException extends GlobalException {

    public static final TooLargeException Default = new TooLargeException();

	public TooLargeException(String message) {
        super(413, message);
    }

    public TooLargeException() {
        super(413, "请求过大");
    }
}
