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
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import work.bottle.demo.HotelBaseServiceStartup;

import java.nio.charset.Charset;

/**
 * TC-020~024: 总开关关闭(bt-response.enable=false).
 * 包装/异常处理全部失效, 行为回到Spring Boot默认; @Ignore语义不受影响.
 */
@SpringBootTest(classes = HotelBaseServiceStartup.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = {"bt-response.enable=false", "bt-response.unknown-key=x"})
@AutoConfigureMockMvc
class EnableDisabledControllerTest {
    private static final Logger logger = LoggerFactory.getLogger(EnableDisabledControllerTest.class);

    @Resource
    private MockMvc mockMvc;

    @Resource
    private TestRestTemplate testRestTemplate;

    @Test
    public void testDisableWrapping() throws Exception {
        // TC-020: 普通对象不再包装, Boolean原样输出
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/index/v1/ret/true")).andReturn();
        String body = mvcResult.getResponse().getContentAsString(Charset.forName("UTF-8"));
        logger.info("GET /index/v1/ret/true: {}", body);
        Assert.assertEquals("关闭后应返回裸布尔值", "true", body);
    }

    @Test
    public void testDisableOperationExceptionHandling() {
        // TC-021: OperationException不被处理, 走Boot默认错误体(无success/code结构)
        ResponseEntity<String> entity = testRestTemplate.getForEntity("/index/v1/err/opt/exception", String.class);
        logger.info("GET /index/v1/err/opt/exception: {} {}", entity.getStatusCode(), entity.getBody());
        Assert.assertEquals(500, entity.getStatusCode().value());
        DocumentContext body = JsonPath.parse(entity.getBody());
        Assert.assertEquals(500, body.read("$.status", Integer.class).intValue());
        Assert.assertEquals("Internal Server Error", body.read("$.error", String.class));
        Assert.assertThrows("不应存在starter包装结构", PathNotFoundException.class, () -> body.read("$.success"));
    }

    @Test
    public void testDisableGlobalExceptionHandling() {
        // TC-022: GlobalException不被处理, 回落为500默认错误体
        ResponseEntity<String> entity = testRestTemplate.getForEntity("/index/v1/e/s/404", String.class);
        logger.info("GET /index/v1/e/s/404: {} {}", entity.getStatusCode(), entity.getBody());
        Assert.assertEquals(500, entity.getStatusCode().value());
        DocumentContext body = JsonPath.parse(entity.getBody());
        Assert.assertEquals("Internal Server Error", body.read("$.error", String.class));
    }

    @Test
    public void testDisableIgnoreStillRaw() throws Exception {
        // TC-023: @Ignore语义与开关无关
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/method/ignore/v1/str")).andReturn();
        String body = mvcResult.getResponse().getContentAsString(Charset.forName("UTF-8"));
        Assert.assertEquals("hello world", body);
    }

    @Test
    public void testDisableClassLevelIgnoreStillRaw() throws Exception {
        // 类级@Ignore: 裸map返回, 无包装结构
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/ignore/v1/map")).andReturn();
        String body = mvcResult.getResponse().getContentAsString(Charset.forName("UTF-8"));
        logger.info("GET /ignore/v1/map: {}", body);
        DocumentContext doc = JsonPath.parse(body);
        Assert.assertEquals("world", doc.read("$.hello", String.class));
        Assert.assertThrows(PathNotFoundException.class, () -> doc.read("$.success"));
    }
}
