/**
* 服务器在资源处理时出现配置错误.
**/
package work.bottle.plugin.exception.global.base;

import work.bottle.plugin.exception.GlobalException;

public final class ConfigurationException extends GlobalException {

    public static final ConfigurationException Default = new ConfigurationException();

    public ConfigurationException() {
        super(506, "服务器配置错误");
    }
}
