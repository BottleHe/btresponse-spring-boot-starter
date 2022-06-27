package work.bottle.plugin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.util.List;

@Configuration
public class BtHttpResponseConverterConfig extends WebMvcConfigurationSupport {

    private static final Logger logger = LoggerFactory.getLogger(BtHttpResponseConverterConfig.class);

    @Override
    protected void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        logger.debug("BtHttpResponseConverterConfig::configureMessageConverters({})", converters);
        BtResponseProperties properties = getApplicationContext().getBean(BtResponseProperties.class);
        // logger.debug("---> {}, {}", properties, properties.isEnable());
        // 如果是强制模式, 将JSON 转换器放到最前的位置
        if (null != properties && null != properties.getForce() && properties.getForce()) {
            converters.add(0, new BtMappingJackson2HttpMessageConverter());
        }
        super.addDefaultHttpMessageConverters(converters);
    }
}
