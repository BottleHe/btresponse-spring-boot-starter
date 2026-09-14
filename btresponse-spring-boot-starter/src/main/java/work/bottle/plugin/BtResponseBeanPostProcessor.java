package work.bottle.plugin;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.http.converter.*;
import org.springframework.http.converter.support.AllEncompassingFormHttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

@Component
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@ConditionalOnBean({ RequestMappingHandlerAdapter.class })
@ConditionalOnExpression("${bt-response.enable:true} && ${bt-response.force:false}")
public class BtResponseBeanPostProcessor implements BeanPostProcessor {

    private final ObjectProvider<ObjectMapper> objectMapperProvider;

    public BtResponseBeanPostProcessor(ObjectProvider<ObjectMapper> objectMapperProvider) {
        this.objectMapperProvider = objectMapperProvider;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof RequestMappingHandlerAdapter && beanName.equals("requestMappingHandlerAdapter")) {
            // logger.debug("BtResponseBeanPostProcessor::postProcessAfterInitialization({}, {})", bean, beanName);
            // 优先使用容器中的ObjectMapper, 保留使用方的 spring.jackson.* 定制; 没有时使用兜底配置.
            ObjectMapper objectMapper = objectMapperProvider.getIfAvailable(BtMappingJackson2HttpMessageConverter::buildDefaultObjectMapper);
            objectMapper = BtMappingJackson2HttpMessageConverter.enforceForceWritable(objectMapper);
            List<HttpMessageConverter<?>> messageConverters = ((RequestMappingHandlerAdapter) bean).getMessageConverters();
            if (null != messageConverters) {
                messageConverters.add(0, new BtMappingJackson2HttpMessageConverter(objectMapper));
            } else {
                messageConverters = new ArrayList<>();
                messageConverters.add(new BtMappingJackson2HttpMessageConverter(objectMapper));
                addDefaultHttpMessageConverters(messageConverters);
                ((RequestMappingHandlerAdapter) bean).setMessageConverters(messageConverters);
            }
        }
        return bean;
    }

    protected final void addDefaultHttpMessageConverters(List<HttpMessageConverter<?>> messageConverters) {
        messageConverters.add(new ByteArrayHttpMessageConverter());
        messageConverters.add(new StringHttpMessageConverter(Charset.forName("UTF-8")));
        messageConverters.add(new ResourceHttpMessageConverter());
        messageConverters.add(new ResourceRegionHttpMessageConverter());
        messageConverters.add(new AllEncompassingFormHttpMessageConverter());
    }
}
