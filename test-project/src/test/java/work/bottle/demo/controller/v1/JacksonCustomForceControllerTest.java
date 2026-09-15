package work.bottle.demo.controller.v1;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.PathNotFoundException;
import jakarta.annotation.Resource;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import work.bottle.demo.HotelBaseServiceStartup;

import java.nio.charset.Charset;

/**
 * TC-050/052: 强制模式下的Jackson定制复用与@Ignore优先级.
 * force模式的包装响应应使用容器中的ObjectMapper, 保留spring.jackson.*定制.
 */
@SpringBootTest(classes = HotelBaseServiceStartup.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = {"bt-response.force=true", "spring.jackson.property-naming-strategy=SNAKE_CASE"})
@AutoConfigureMockMvc
class JacksonCustomForceControllerTest {
    private static final Logger logger = LoggerFactory.getLogger(JacksonCustomForceControllerTest.class);

    @Resource
    private MockMvc mockMvc;

    private String get(String uri) throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(uri)).andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        String body = response.getContentAsString(Charset.forName("UTF-8"));
        logger.info("GET {}: {} {}", uri, response.getStatus(), body);
        return body;
    }

    @Test
    public void testForceReusesContainerObjectMapper() throws Exception {
        // TC-050: expireTimestamp应按SNAKE_CASE输出为expire_timestamp
        DocumentContext body = JsonPath.parse(get("/index/v1/code/send"));
        Assert.assertEquals(Boolean.TRUE, body.read("$.success", Boolean.class));
        Assert.assertNotNull("snake_case字段应存在(容器ObjectMapper定制生效)",
                body.read("$.data.expire_timestamp", Integer.class));
        Assert.assertThrows("camelCase字段不应存在", PathNotFoundException.class,
                () -> body.read("$.data.expireTimestamp"));
    }

    @Test
    public void testForceDoesNotOverrideIgnore() throws Exception {
        // TC-052基线: @Ignore跳过包装, 但force模式的JSON序列化仍然生效——String输出为JSON字符串(带引号)
        Assert.assertEquals("\"hello world\"", get("/method/ignore/v1/str"));
    }
}
