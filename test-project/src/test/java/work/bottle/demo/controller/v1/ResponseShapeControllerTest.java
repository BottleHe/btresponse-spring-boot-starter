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
 * TC-030~035: 返回结构矩阵(默认工厂).
 * isInstance透传, void基线, ResponseEntity各形态, Map包装.
 */
@SpringBootTest(classes = HotelBaseServiceStartup.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class ResponseShapeControllerTest {
    private static final Logger logger = LoggerFactory.getLogger(ResponseShapeControllerTest.class);

    @Resource
    private MockMvc mockMvc;

    private DocumentContext perform(String uri, int expectedStatus) throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(uri)).andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        String body = response.getContentAsString(Charset.forName("UTF-8"));
        logger.info("GET {}: {} {}", uri, response.getStatus(), body);
        Assert.assertEquals("http status异常(" + uri + ")", expectedStatus, response.getStatus());
        return JsonPath.parse(body);
    }

    @Test
    public void testBtResponsePassthrough() throws Exception {
        // TC-030: 已是BtResponse -> isInstance透传, 不二次包装
        DocumentContext body = perform("/index/v1/ret/btresponse", 200);
        Assert.assertEquals(Boolean.FALSE, body.read("$.success", Boolean.class));
        Assert.assertEquals(1234, body.read("$.code", Integer.class).intValue());
        Assert.assertEquals("预包装返回", body.read("$.message", String.class));
        Assert.assertEquals(Boolean.TRUE, body.read("$.data.passthrough", Boolean.class));
        Assert.assertThrows("不应出现双层包装", PathNotFoundException.class, () -> body.read("$.data.data"));
    }

    @Test
    public void testCustomResponseWrappedAsData() throws Exception {
        // 默认工厂不识别CustomResponse, 整体作为data包装
        DocumentContext body = perform("/index/v1/ret/custom-response", 200);
        Assert.assertEquals(Boolean.TRUE, body.read("$.success", Boolean.class));
        Assert.assertEquals(0, body.read("$.code", Integer.class).intValue());
        Assert.assertEquals(88, body.read("$.data.code", Integer.class).intValue());
        Assert.assertEquals("自定义结构", body.read("$.data.msg", String.class));
        Assert.assertEquals("payload", body.read("$.data.data", String.class));
    }

    @Test
    public void testVoidReturnBaseline() throws Exception {
        // TC-032: void返回触达produceDefaultResponse分支, 输出默认包装(data为null)——实测基线
        DocumentContext body = perform("/index/v1/ret/void", 200);
        Assert.assertEquals(Boolean.TRUE, body.read("$.success", Boolean.class));
        Assert.assertEquals(0, body.read("$.code", Integer.class).intValue());
        Assert.assertTrue("void包装的data应为null", body.read("$.data") == null);
    }

    @Test
    public void testResponseEntityStringRaw() throws Exception {
        // TC-033: ResponseEntity<String>的String体走StringHttpMessageConverter, 原样返回
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/index/v1/ret/response-entity-string")).andReturn();
        Assert.assertEquals(200, mvcResult.getResponse().getStatus());
        Assert.assertEquals("entity-body", mvcResult.getResponse().getContentAsString(Charset.forName("UTF-8")));
    }

    @Test
    public void testResponseEntityBtPassthrough() throws Exception {
        // TC-034: ResponseEntity<BtResponse>透传且保留201状态
        DocumentContext body = perform("/index/v1/ret/response-entity-bt", 201);
        Assert.assertEquals(Boolean.TRUE, body.read("$.success", Boolean.class));
        Assert.assertEquals(0, body.read("$.code", Integer.class).intValue());
        Assert.assertEquals("created", body.read("$.data", String.class));
    }

    @Test
    public void testMapWrapped() throws Exception {
        // TC-035: Map返回正常包装进data
        DocumentContext body = perform("/index/v1/ret/map", 200);
        Assert.assertEquals(Boolean.TRUE, body.read("$.success", Boolean.class));
        Assert.assertEquals("world", body.read("$.data.hello", String.class));
        Assert.assertEquals(2, body.read("$.data.count", Integer.class).intValue());
    }
}
