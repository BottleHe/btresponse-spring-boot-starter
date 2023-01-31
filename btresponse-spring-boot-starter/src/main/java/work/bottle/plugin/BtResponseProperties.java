package work.bottle.plugin;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Optional;

@Configuration
@ConfigurationProperties(prefix = "bt-response", ignoreUnknownFields = true)
public class BtResponseProperties {

    /**
     * 开关
     */
    private Boolean enable;

    /**
     * 强制模式, 默认为false.
     * 如果为强制模式时. 返回格式固定.
     * 非强制模式时, 处理方式同spring 预定义模式.
     */
    private Boolean force;

    /**
     * 宽松模式, 主要是控制在异常出现时的http status. 默认为false, 当出现非业务异常时, http status肯定不是200.
     * 如果为true, 则出现可控异常时. http status固定为200. 通过用户自定义code去确认异常.
     */
    private Boolean looseMode;

    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

    public boolean isEnable() {
        return null != enable && enable;
    }

    public Boolean getForce() {
        return force;
    }

    public void setForce(Boolean force) {
        this.force = force;
    }

    public boolean isForce() {
        return null != force && force;
    }

    public Boolean getLooseMode() {
        return looseMode;
    }

    public void setLooseMode(Boolean looseMode) {
        this.looseMode = looseMode;
    }

    public boolean isLooseMode() {
        return null != looseMode && looseMode;
    }

    @Override
    public String toString() {
        return "BtResponseProperties{" +
                "enable=" + enable +
                ", force=" + force +
                ", looseMode=" + looseMode +
                '}';
    }
}
