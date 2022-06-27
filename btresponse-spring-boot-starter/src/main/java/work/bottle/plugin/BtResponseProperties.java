package work.bottle.plugin;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "bt-response", ignoreUnknownFields = true)
public class BtResponseProperties {

    private Boolean enable;

    private Boolean force;

    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

    public Boolean getForce() {
        return force;
    }

    public void setForce(Boolean force) {
        this.force = force;
    }

    @Override
    public String toString() {
        return "BtResponseProperties{" +
                "enable=" + enable +
                ", force=" + force +
                '}';
    }
}
