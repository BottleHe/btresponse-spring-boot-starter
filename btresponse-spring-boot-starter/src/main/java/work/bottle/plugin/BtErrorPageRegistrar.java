package work.bottle.plugin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.autoconfigure.web.servlet.DispatcherServletPath;
import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.ErrorPageRegistrar;
import org.springframework.boot.web.server.ErrorPageRegistry;

/**
 * 错误页面路由注入.
 */
public class BtErrorPageRegistrar implements ErrorPageRegistrar {
    private static final Logger logger = LoggerFactory.getLogger(BtErrorPageRegistrar.class);

    private final ServerProperties properties;

    private final DispatcherServletPath dispatcherServletPath;

    public BtErrorPageRegistrar(ServerProperties properties, DispatcherServletPath dispatcherServletPath) {
        this.properties = properties;
        this.dispatcherServletPath = dispatcherServletPath;
    }

    @Override
    public void registerErrorPages(ErrorPageRegistry registry) {
        if (logger.isDebugEnabled()) {
            logger.debug("ErrorPage Path is : {}", this.properties.getError().getPath());
        }
        registry.addErrorPages(new ErrorPage(this.dispatcherServletPath.getRelativePath(this.properties.getError().getPath())));
    }
}
