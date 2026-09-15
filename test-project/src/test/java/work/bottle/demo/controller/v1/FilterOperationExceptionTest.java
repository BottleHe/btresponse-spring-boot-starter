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
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.ResponseEntity;
import work.bottle.demo.HotelBaseServiceStartup;
import work.bottle.plugin.exception.OperationException;

/**
 * TC-010: Filter中抛出OperationException.
 * 已知行为(记录为基线): 该异常不经过Handler异常处理链, 走error page -> BasicErrorController -> Advice包装,
 * 业务code(10100)会丢失变成500. 若未来要保留业务code, 需增强BtErrorController对ERROR_EXCEPTION的处理.
 */
@SpringBootTest(classes = HotelBaseServiceStartup.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Import(FilterOperationExceptionTest.ThrowingFilterConfig.class)
class FilterOperationExceptionTest {
    private static final Logger logger = LoggerFactory.getLogger(FilterOperationExceptionTest.class);

    @Configuration
    static class ThrowingFilterConfig {

        @Bean
        public FilterRegistrationBean<jakarta.servlet.Filter> throwingFilter() {
            FilterRegistrationBean<jakarta.servlet.Filter> registration = new FilterRegistrationBean<>();
            registration.setFilter((request, response, chain) -> {
                throw new OperationException(10100, "Operation Exception");
            });
            registration.addUrlPatterns("/filter/throw/*");
            return registration;
        }
    }

    @Resource
    private TestRestTemplate testRestTemplate;

    @Test
    public void testOperationExceptionFromFilter() {
        ResponseEntity<String> entity = testRestTemplate.getForEntity("/filter/throw/oe", String.class);
        logger.info("GET /filter/throw/oe: {} {}", entity.getStatusCode(), entity.getBody());
        Assert.assertEquals(500, entity.getStatusCode().value());
        DocumentContext body = JsonPath.parse(entity.getBody());
        // 当前基线: 统一格式但业务code丢失(500而非10100)
        Assert.assertEquals(Boolean.FALSE, body.read("$.success", Boolean.class));
        Assert.assertEquals(500, body.read("$.code", Integer.class).intValue());
        Assert.assertEquals("/filter/throw/oe", body.read("$.data.path", String.class));
    }
}
