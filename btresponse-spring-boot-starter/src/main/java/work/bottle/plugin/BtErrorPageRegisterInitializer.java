package work.bottle.plugin;

import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.ErrorPageRegistrar;
import org.springframework.boot.web.server.ErrorPageRegistry;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;

public class BtErrorPageRegisterInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        // System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>BTErrorPageRegisterInitializer::initialize(" + applicationContext.getClass().getName() + ");");

        // 直接注入错误配置单例
        /* ConfigurableListableBeanFactory beanFactory = applicationContext.getBeanFactory();
        beanFactory.registerSingleton("errorPageRegistrar", new ErrorPageRegistrar() {
            @Override
            public void registerErrorPages(ErrorPageRegistry registry) {
                registry.addErrorPages(new ErrorPage("/____error"));
            }
        });
        beanFactory.registerSingleton("", ); */
        applicationContext.addBeanFactoryPostProcessor(new BtResponseBeanFactoryPostProcessor());
    }
}
