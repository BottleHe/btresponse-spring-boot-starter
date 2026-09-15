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
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import work.bottle.demo.HotelBaseServiceStartup;

import java.nio.charset.Charset;

/**
 * TC-006/007: 宽松模式(loose-mode=true).
 * 可控异常的http status固定为200, 业务code保留在body中.
 */
@SpringBootTest(classes = HotelBaseServiceStartup.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = {"bt-response.loose-mode=true"})
@AutoConfigureMockMvc
class LooseModeControllerTest {
    private static final Logger logger = LoggerFactory.getLogger(LooseModeControllerTest.class);

    @Resource
    private MockMvc mockMvc;

    @Test
    public void testLooseModeGlobalException() throws Exception {
        final String uri = "/index/v1/e/s/404";
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(uri)).andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        String body = response.getContentAsString(Charset.forName("UTF-8"));
        logger.info("GET {}: {} {}", uri, response.getStatus(), body);
        Assert.assertEquals("宽松模式下GlobalException的http status应为200", 200, response.getStatus());
        DocumentContext doc = JsonPath.parse(body);
        Assert.assertEquals(Boolean.FALSE, doc.read("$.success", Boolean.class));
        Assert.assertEquals("业务code应保留", 404, doc.read("$.code", Integer.class).intValue());
    }

    @Test
    public void testLooseModeBindException() throws Exception {
        final String uri = "/index/v1/valid/body";
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"page\":0,\"size\":10}");
        MvcResult mvcResult = mockMvc.perform(request).andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        String body = response.getContentAsString(Charset.forName("UTF-8"));
        logger.info("POST {}: {} {}", uri, response.getStatus(), body);
        Assert.assertEquals("宽松模式下BindException的http status应为200", 200, response.getStatus());
        DocumentContext doc = JsonPath.parse(body);
        Assert.assertEquals(422, doc.read("$.code", Integer.class).intValue());
        Assert.assertEquals("page必须大于等于1", doc.read("$.message", String.class));
    }
}
