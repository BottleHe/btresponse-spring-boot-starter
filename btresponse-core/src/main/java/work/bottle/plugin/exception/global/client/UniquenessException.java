/**
* 一般是提交了重复的数据请求(比如重复下单, 重复注册<用户已存在>等)
**/
package work.bottle.plugin.exception.global.client;

import work.bottle.plugin.exception.GlobalException;
import work.bottle.plugin.exception.global.GlobalError;

public final class UniquenessException extends GlobalException {

    public static final UniquenessException Default = (UniquenessException) GlobalError.getInstance().buildDefaultByCode(463);

	public UniquenessException(String message) {
        super(463, message);
    }

    public UniquenessException() {
        super(463, "唯一性异常");
    }

    public UniquenessException(String message, Object data) {
        super(463, message, data);
    }

    public UniquenessException(String message, Object data, Throwable t) {
        super(463, message, data, t);
    }
}
