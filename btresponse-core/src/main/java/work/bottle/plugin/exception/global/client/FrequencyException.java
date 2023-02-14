/**
* 一般存在于限制访问频率的接口, 用户访问频率过高
**/
package work.bottle.plugin.exception.global.client;

import work.bottle.plugin.exception.GlobalException;

public final class FrequencyException extends GlobalException {

    public static final FrequencyException Default = new FrequencyException();

	public FrequencyException(String message) {
        super(429, message);
    }

    public FrequencyException() {
        super(429, "访问过于频繁, 请稍候再试");
    }
}
