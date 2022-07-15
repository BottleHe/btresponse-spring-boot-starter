package work.bottle.plugin;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

@ConditionalOnProperty(name = "bt-response.enable", matchIfMissing = true)
public class BtBeanDefinitionRegistryPostProcessor implements BeanDefinitionRegistryPostProcessor {
    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        registry.registerBeanDefinition("btResponseProperties",
                BeanDefinitionBuilder.genericBeanDefinition(BtResponseProperties.class).getBeanDefinition());
        registry.registerBeanDefinition("btResponseConfig",
                BeanDefinitionBuilder.genericBeanDefinition(BtResponseConfig.class).getBeanDefinition());
        if (registry.containsBeanDefinition("basicErrorController")) {
            registry.removeBeanDefinition("basicErrorController");
        }
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
    }
}
