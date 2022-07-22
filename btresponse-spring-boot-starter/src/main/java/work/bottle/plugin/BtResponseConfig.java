package work.bottle.plugin;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.*;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.autoconfigure.web.servlet.DispatcherServletPath;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcProperties;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorViewResolver;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.server.ErrorPageRegistrar;
import org.springframework.boot.web.server.ErrorPageRegistrarBeanPostProcessor;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.Servlet;
import java.util.stream.Collectors;

@AutoConfiguration(before = WebMvcAutoConfiguration.class)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@ConditionalOnClass({ Servlet.class, DispatcherServlet.class })
@EnableConfigurationProperties({ ServerProperties.class, BtResponseProperties.class, WebMvcProperties.class})
public class BtResponseConfig {

    private final ServerProperties serverProperties;
    private final BtResponseProperties btResponseProperties;

    public BtResponseConfig(ServerProperties serverProperties, BtResponseProperties btResponseProperties) {
        this.serverProperties = serverProperties;
        this.btResponseProperties = btResponseProperties;
    }

    @Bean
    @ConditionalOnMissingBean(value = ErrorAttributes.class, search = SearchStrategy.CURRENT)
    public DefaultErrorAttributes errorAttributes() {
        return new DefaultErrorAttributes();
    }

    /**
     * 定义一个处理tomcat 异常处理的Bean. 方式1
     * 我这里有注入时机问题, 所以通过 ApplicationContextInitializer 直接注入singleton
     */
    @Bean
    public ErrorPageRegistrar errorPageRegistrar(DispatcherServletPath dispatcherServletPath) {
        return new BtErrorPageRegistrar(this.serverProperties, dispatcherServletPath);
    }

    // 添加一个 关于 tomcat 错误页面的 bean 后处理器
    @Bean
    @ConditionalOnMissingBean(ErrorPageRegistrarBeanPostProcessor.class)
    public ErrorPageRegistrarBeanPostProcessor errorPageRegistrarBeanPostProcessor() {
        return new ErrorPageRegistrarBeanPostProcessor(); // 在容器中找到所有的 ErrorPageRegistrar , 并回调WebServerFactory, 向其中添加 ErrorPageRegistry
    }

    @Bean
    @ConditionalOnMissingBean(value = ErrorController.class, search = SearchStrategy.CURRENT)
    public BtErrorController btErrorController(ErrorAttributes errorAttributes,
                                               ObjectProvider<ErrorViewResolver> errorViewResolvers) {
        return new BtErrorController(errorAttributes, this.serverProperties.getError(), this.btResponseProperties,
                errorViewResolvers.orderedStream().collect(Collectors.toList()));
    }
}
