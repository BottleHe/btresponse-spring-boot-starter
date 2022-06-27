package work.bottle.demo.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import java.util.List;

public class BtApplicationEventListener implements ApplicationListener<WebServerInitializedEvent>, ApplicationContextAware {
    private static final Logger logger = LoggerFactory.getLogger(BtApplicationEventListener.class);

    private ApplicationContext applicationContext;

    @Override
    public void onApplicationEvent(WebServerInitializedEvent event) {
        logger.info("事件被监听到: {}, context: {}", event, this.applicationContext);
        RequestMappingHandlerAdapter adapter = applicationContext.getBean(RequestMappingHandlerAdapter.class);
        // adapter.setCustomReturnValueHandlers(com.sun.tools.javac.util.List.of(new BtReturnValueHandler()));
        List<HandlerMethodReturnValueHandler> returnValueHandlers = adapter.getReturnValueHandlers();

        logger.info("ReturnValueHandlers: ");
        returnValueHandlers.forEach(handlerMethodReturnValueHandler -> {
            logger.info("  - " + handlerMethodReturnValueHandler);
        });
        logger.info("CustomerReturnValueHandlers: ");
        adapter.getCustomReturnValueHandlers().forEach(handler -> {
            logger.info("  - " + handler);
        });
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
