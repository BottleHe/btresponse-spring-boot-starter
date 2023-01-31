/**
* 一般是提交了重复的数据请求(比如重复下单, 重复注册<用户已存在>等)
**/
package work.bottle.plugin.exception.global.client;

import work.bottle.plugin.exception.GlobalException;

public final class UniquenessException extends GlobalException {

    public static final UniquenessException Default = new UniquenessException();

    public UniquenessException() {
        super(463, "唯一性异常");
    }
}
