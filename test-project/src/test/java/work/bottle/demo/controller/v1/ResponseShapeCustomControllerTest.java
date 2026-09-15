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
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import work.bottle.demo.HotelBaseServiceStartup;
import work.bottle.demo.config.CustomerResponseFactory;

import java.nio.charset.Charset;

/**
 * TC-031: 自定义工厂场景下的isInstance透传.
 */
@SpringBootTest(classes = HotelBaseServiceStartup.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Import(CustomerResponseFactory.class)
class ResponseShapeCustomControllerTest {
    private static final Logger logger = LoggerFactory.getLogger(ResponseShapeCustomControllerTest.class);

    @Resource
    private MockMvc mockMvc;

    private DocumentContext perform(String uri) throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(uri)).andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        String body = response.getContentAsString(Charset.forName("UTF-8"));
        logger.info("GET {}: {} {}", uri, response.getStatus(), body);
        Assert.assertEquals(200, response.getStatus());
        return JsonPath.parse(body);
    }

    @Test
    public void testCustomResponsePassthrough() throws Exception {
        // CustomResponse对自定义工厂透传, 不再被包装为data
        DocumentContext body = perform("/index/v1/ret/custom-response");
        Assert.assertEquals(88, body.read("$.code", Integer.class).intValue());
        Assert.assertEquals("自定义结构", body.read("$.msg", String.class));
        Assert.assertEquals("payload", body.read("$.data", String.class));
        Assert.assertThrows("自定义结构外层不应有success字段", PathNotFoundException.class, () -> body.read("$.success"));
    }

    @Test
    public void testBtResponseWrappedAsDataUnderCustomFactory() throws Exception {
        // 自定义工厂不识别BtResponse, 反而被当作data包装(BtResponse序列化后字段为message而非msg)
        DocumentContext body = perform("/index/v1/ret/btresponse");
        Assert.assertEquals(0, body.read("$.code", Integer.class).intValue());
        Assert.assertEquals(1234, body.read("$.data.code", Integer.class).intValue());
        Assert.assertEquals("预包装返回", body.read("$.data.message", String.class));
    }
}
