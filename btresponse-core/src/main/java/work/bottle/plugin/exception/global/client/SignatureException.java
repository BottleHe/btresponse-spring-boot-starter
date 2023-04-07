/**
* 一般是某些访问需要数据签名的情况 . 
**/
package work.bottle.plugin.exception.global.client;

import work.bottle.plugin.exception.GlobalException;

public final class SignatureException extends GlobalException {

    public static final SignatureException Default = (SignatureException) GlobalError.getInstance().buildDefaultByCode(464);

	public SignatureException(String message) {
        super(464, message);
    }

    public SignatureException() {
        super(464, "签名异常");
    }

    public SignatureException(String message, Object data) {
        super(464, message, data);
    }

    public SignatureException(String message, Object data, Throwable t) {
        super(464, message, data, t);
    }
}
