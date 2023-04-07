/**
* 访问的资源/文件/数据等不存在时抛出的异常
**/
package work.bottle.plugin.exception.global.client;

import work.bottle.plugin.exception.GlobalException;
import work.bottle.plugin.exception.global.GlobalError;

public final class NotFoundException extends GlobalException {

	public static final NotFoundException Default = new NotFoundException();

	public NotFoundException(String message) {
        super(404, message);
    }

    public NotFoundException() {
        super(404, "访问内容不存在");
    }

    public NotFoundException(String message, Object data) {
        super(404, message, data);
    }

    public NotFoundException(String message, Object data, Throwable t) {
        super(404, message, data, t);
    }
}
