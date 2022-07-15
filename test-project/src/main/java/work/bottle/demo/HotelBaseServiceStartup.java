package work.bottle.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.Collection;
import java.util.List;
import java.util.function.BiConsumer;

@SpringBootApplication
public class HotelBaseServiceStartup {

    public static void main(String[] args) {
        try {
            ConfigurableApplicationContext context = SpringApplication.run(HotelBaseServiceStartup.class, args);

            for (String beanDefinitionName : context.getBeanDefinitionNames()) {
                System.out.println(beanDefinitionName);
            }

            System.out.println("\n\n消息转换器列表:");
            RequestMappingHandlerAdapter requestMappingHandlerAdapter = context.getBean("requestMappingHandlerAdapter", RequestMappingHandlerAdapter.class);
            List<HttpMessageConverter<?>> messageConverters = requestMappingHandlerAdapter.getMessageConverters();
            messageConverters.forEach(httpMessageConverter -> {
                System.out.println(httpMessageConverter);
            });

            System.out.println("\n\nRequestMapping:");
            RequestMappingHandlerMapping requestMappingHandlerMapping = context.getBean("requestMappingHandlerMapping", RequestMappingHandlerMapping.class);
            requestMappingHandlerMapping.getHandlerMethods().forEach(new BiConsumer<RequestMappingInfo, HandlerMethod>() {
                @Override
                public void accept(RequestMappingInfo requestMappingInfo, HandlerMethod handlerMethod) {
                    System.out.println("  - " + requestMappingInfo.getMethodsCondition().getMethods() + ":" + requestMappingInfo.getPatternsCondition() + ", HandlerMethod: " + handlerMethod);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
