package work.bottle.plugin;


import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;

public class BtResponseBeanFactoryPostProcessor implements BeanDefinitionRegistryPostProcessor {

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        // BtResponseProperties responseProperties = beanFactory.getBean(BtResponseProperties.class);
        // System.out.println("responseProperties: " + responseProperties.isEnable());
    }

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        // 手动通过BeanFactoryPostProcessor 注入BeanDefinition
//        registry.registerBeanDefinition("errorPageRegistrar",
//                BeanDefinitionBuilder.genericBeanDefinition(BtErrorPageRegistrar.class).getBeanDefinition());
//        registry.registerBeanDefinition("btResponseProperties",
//                BeanDefinitionBuilder.genericBeanDefinition(BtResponseProperties.class).getBeanDefinition());
//        registry.registerBeanDefinition("btResponseConfig",
//                BeanDefinitionBuilder.genericBeanDefinition(BtResponseConfig.class).getBeanDefinition());
        registry.registerBeanDefinition("btHttpResponseConverterConfig",
                BeanDefinitionBuilder.genericBeanDefinition(BtHttpResponseConverterConfig.class).getBeanDefinition());
//        registry.registerBeanDefinition("btResponseBodyAdvice",
//                BeanDefinitionBuilder.genericBeanDefinition(BtResponseBodyAdvice.class).getBeanDefinition());
    }
}
