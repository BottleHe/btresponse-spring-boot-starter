/**
* 访问的资源/文件/数据等不存在时抛出的异常
**/
package work.bottle.plugin.exception.global.base;

import work.bottle.plugin.exception.GlobalException;

public final class NotFoundException extends GlobalException {

    public static final NotFoundException Default = new NotFoundException();

    public NotFoundException() {
        super(404, "访问内容不存在");
    }
}
