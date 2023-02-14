/**
* 不允许访问, 通常因为用户身份, 来源等因素, 区别与Forbidden
**/
package work.bottle.plugin.exception.global.client;

import work.bottle.plugin.exception.GlobalException;

public final class NotAllowedException extends GlobalException {

    public static final NotAllowedException Default = new NotAllowedException();

	public NotAllowedException(String message) {
        super(405, message);
    }

    public NotAllowedException() {
        super(405, "不支持的访问");
    }
}
