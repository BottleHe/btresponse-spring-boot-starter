/**
* 一般是某些访问需要数据签名的情况 . 
**/
package work.bottle.plugin.exception.global.client;

import work.bottle.plugin.exception.GlobalException;

public final class SignatureException extends GlobalException {

    public static final SignatureException Default = new SignatureException();

	public SignatureException(String message) {
        super(464, message);
    }

    public SignatureException() {
        super(464, "签名异常");
    }
}
