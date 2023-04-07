/**
* 一般存在于限制访问频率的接口, 用户访问频率过高
**/
package work.bottle.plugin.exception.global.client;

import work.bottle.plugin.exception.GlobalException;

public final class FrequencyException extends GlobalException {

    public static final FrequencyException Default = (FrequencyException) GlobalError.getInstance().buildDefaultByCode(429);

	public FrequencyException(String message) {
        super(429, message);
    }

    public FrequencyException() {
        super(429, "访问过于频繁, 请稍候再试");
    }

    public FrequencyException(String message, Object data) {
        super(429, message, data);
    }

    public FrequencyException(String message, Object data, Throwable t) {
        super(429, message, data, t);
    }
}
