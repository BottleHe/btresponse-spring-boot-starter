package work.bottle.plugin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.ErrorPageRegistrar;
import org.springframework.boot.web.server.ErrorPageRegistry;

public class BtErrorPageRegistrar implements ErrorPageRegistrar {
    private static final Logger logger = LoggerFactory.getLogger(BtErrorPageRegistrar.class);

    @Value("${server.error.path:${error.path:/____error}}")
    private String errorPagePath;

    @Override
    public void registerErrorPages(ErrorPageRegistry registry) {
        if (logger.isDebugEnabled()) {
            logger.debug("ErrorPage Path is : {}", errorPagePath);
        }
        registry.addErrorPages(new ErrorPage(errorPagePath));
    }
}
