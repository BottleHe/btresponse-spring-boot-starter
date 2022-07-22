package work.bottle.plugin;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Role;

/**
 * 本来的是将它去注入 btResponseProperties和btResponseConfig, 让其在Springboot默认的BasicErrorController被注入前注入.
 * 但是是触发Springboot的 "*** is not eligible for getting processed by all BeanPostProcessors ***" 的告警
 * 现在使用 @AutoConfiguration(before = WebMvcAutoConfiguration.class) 去实现注入.
 */
@Deprecated
public class BtBeanDefinitionRegistryPostProcessor implements BeanDefinitionRegistryPostProcessor {

    // "is not eligible for getting processed by all BeanPostProcessors"
    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
//        registry.registerBeanDefinition("btResponseProperties",
//                BeanDefinitionBuilder.genericBeanDefinition(BtResponseProperties.class).getBeanDefinition());
//        registry.registerBeanDefinition("btResponseConfig",
//                BeanDefinitionBuilder.genericBeanDefinition(BtResponseConfig.class).getBeanDefinition());
        // String[] beanDefinitionNames = registry.getBeanDefinitionNames();
        // registry.registerBeanDefinition("btTypeExcludeFilter", new RootBeanDefinition(BtTypeExcludeFilter.class));
        if (registry.containsBeanDefinition("basicErrorController")) {
            registry.removeBeanDefinition("basicErrorController");
        }
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
    }
}
