package work.bottle.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import work.bottle.demo.model.CustomResponse;
import work.bottle.plugin.BtResponseFactory;
import work.bottle.plugin.StandardResponseFactory;

import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.List;

@Configuration
public class BaseConfig extends WebMvcConfigurationSupport {

//    @Bean
//    public StandardResponseFactory<CustomResponse> standardResponseFactory() {
//        return new CustomerResponseFactory();
//    }

    // 解决中文返回乱码问题
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        if (converters.isEmpty()) {
            addDefaultHttpMessageConverters(converters);
        }
        Iterator<HttpMessageConverter<?>> iterator = converters.iterator();
        boolean changed = false;
        while (iterator.hasNext()) {
            HttpMessageConverter<?> next = iterator.next();
            if (next instanceof StringHttpMessageConverter) {
                ((StringHttpMessageConverter) next).setDefaultCharset(Charset.forName("UTF-8"));
                changed = true;
                break;
            }
        }
        if (!changed) {
            converters.add(new StringHttpMessageConverter(Charset.forName("UTF-8")));
        }
        super.configureMessageConverters(converters);
    }

    @Override
    protected void addInterceptors(InterceptorRegistry registry) {
        super.addInterceptors(registry);
    }
}
