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
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import work.bottle.demo.HotelBaseServiceStartup;

import java.nio.charset.Charset;

/**
 * TC-001/002/003: GlobalException处理链.
 * code作为http status返回(含非标准HTTP code 461/561), 自定义message/data透传.
 */
@SpringBootTest(classes = HotelBaseServiceStartup.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class GlobalExceptionControllerTest {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionControllerTest.class);

    @Resource
    private MockMvc mockMvc;

    private DocumentContext perform(String uri, int expectedStatus) throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(uri)).andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        String body = response.getContentAsString(Charset.forName("UTF-8"));
        logger.info("GET {}: {} {}", uri, response.getStatus(), body);
        Assert.assertEquals("http status异常(GET " + uri + ")", expectedStatus, response.getStatus());
        return JsonPath.parse(body);
    }

    @Test
    public void testGlobalException404() throws Exception {
        DocumentContext body = perform("/index/v1/e/s/404", 404);
        Assert.assertEquals(Boolean.FALSE, body.read("$.success", Boolean.class));
        Assert.assertEquals(404, body.read("$.code", Integer.class).intValue());
        Assert.assertEquals("访问内容不存在", body.read("$.message", String.class));
        Assert.assertTrue("data应为null", body.read("$.data") == null);
    }

    @Test
    public void testGlobalException506NonStandardCode() throws Exception {
        // 506为HTTP标准但非通用状态码, 验证code原样透传为http status
        DocumentContext body = perform("/index/v1/e/s/506", 506);
        Assert.assertEquals(506, body.read("$.code", Integer.class).intValue());
        Assert.assertEquals("服务器配置错误", body.read("$.message", String.class));
    }

    @Test
    public void testGlobalException461NonHttpCode() throws Exception {
        // 461非HTTP标准状态码, 验证业务code透传为自定义http status
        DocumentContext body = perform("/index/v1/e/s/461", 461);
        Assert.assertEquals(461, body.read("$.code", Integer.class).intValue());
        Assert.assertEquals("身份未知", body.read("$.message", String.class));
    }

    @Test
    public void testGlobalException561NonHttpCode() throws Exception {
        DocumentContext body = perform("/index/v1/e/s/561", 561);
        Assert.assertEquals(561, body.read("$.code", Integer.class).intValue());
        Assert.assertEquals("缺少可用资源", body.read("$.message", String.class));
    }

    @Test
    public void testGlobalExceptionCustomMessageAndData() throws Exception {
        DocumentContext body = perform("/index/v1/e/s/409-custom", 409);
        Assert.assertEquals(Boolean.FALSE, body.read("$.success", Boolean.class));
        Assert.assertEquals(409, body.read("$.code", Integer.class).intValue());
        Assert.assertEquals("订单状态已变更, 请刷新后重试", body.read("$.message", String.class));
        Assert.assertEquals(9527, body.read("$.data.orderId", Integer.class).intValue());
    }
}
