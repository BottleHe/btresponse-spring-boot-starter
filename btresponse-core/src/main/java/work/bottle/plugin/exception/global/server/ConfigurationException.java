/**
* 服务器在资源处理时出现配置错误.
**/
package work.bottle.plugin.exception.global.server;

import work.bottle.plugin.exception.GlobalException;

public final class ConfigurationException extends GlobalException {

    public static final ConfigurationException Default = (ConfigurationException) GlobalError.getInstance().buildDefaultByCode(506);

	public ConfigurationException(String message) {
        super(506, message);
    }

    public ConfigurationException() {
        super(506, "服务器配置错误");
    }

    public ConfigurationException(String message, Object data) {
        super(506, message, data);
    }

    public ConfigurationException(String message, Object data, Throwable t) {
        super(506, message, data, t);
    }
}
