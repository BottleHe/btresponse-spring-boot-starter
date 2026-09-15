package work.bottle.demo.controller.v1;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import jakarta.annotation.Resource;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import work.bottle.demo.HotelBaseServiceStartup;

import java.util.Collections;
import java.util.Map;

/**
 * TC-013: 使用方自定义ErrorController时, starter的BtErrorController与Boot的BasicErrorController都应让位
 * (@ConditionalOnMissingBean(ErrorController)), 且Advice会代理包装自定义控制器的返回.
 */
@SpringBootTest(classes = HotelBaseServiceStartup.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Import(CustomErrorControllerTest.CustomErrorControllerConfig.class)
class CustomErrorControllerTest {
    private static final Logger logger = LoggerFactory.getLogger(CustomErrorControllerTest.class);

    @Configuration
    static class CustomErrorControllerConfig {

        @Bean
        public ErrorController customErrorController() {
            return new CustomMarkerErrorController();
        }
    }

    @RestController
    static class CustomMarkerErrorController implements ErrorController {

        @RequestMapping("${server.error.path:/error}")
        @ResponseBody
        public Map<String, Object> error() {
            return Collections.singletonMap("marker", "custom-error-controller");
        }
    }

    @Resource
    private TestRestTemplate testRestTemplate;

    @Test
    public void testCustomErrorControllerTakesOver() {
        // 未处理异常触发error分发, 应由自定义ErrorController处理(而非BtErrorController/BasicErrorController),
        // 其Map返回再被Advice包装进data
        ResponseEntity<String> entity = testRestTemplate.getForEntity("/index/v1/err/exception", String.class);
        logger.info("GET /index/v1/err/exception: {} {}", entity.getStatusCode(), entity.getBody());
        Assert.assertEquals(500, entity.getStatusCode().value());
        DocumentContext body = JsonPath.parse(entity.getBody());
        Assert.assertEquals("custom-error-controller", body.read("$.data.marker", String.class));
    }
}
