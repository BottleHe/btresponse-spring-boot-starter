/**
* 服务器无法接受客户端的访问
**/
package work.bottle.plugin.exception.global.base;

import work.bottle.plugin.exception.GlobalException;

public final class NotAcceptableException extends GlobalException {

    public static final NotAcceptableException Default = new NotAcceptableException();

    public NotAcceptableException() {
        super(406, "不接受的访问");
    }
}
