package work.bottle.demo.controller.v1;

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
 * TC-024: enable=false优先于force=true, 一切关闭.
 */
@SpringBootTest(classes = HotelBaseServiceStartup.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = {"bt-response.enable=false", "bt-response.force=true"})
@AutoConfigureMockMvc
class EnableDisabledForceControllerTest {
    private static final Logger logger = LoggerFactory.getLogger(EnableDisabledForceControllerTest.class);

    @Resource
    private MockMvc mockMvc;

    private String get(String uri) throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(uri)).andReturn();
        String body = mvcResult.getResponse().getContentAsString(Charset.forName("UTF-8"));
        logger.info("GET {}: {}", uri, body);
        return body;
    }

    @Test
    public void testEnableWinsOverForce() throws Exception {
        // String不包装
        Assert.assertEquals("hello world", get("/index/v1/ret/str"));
        // Boolean返回裸值, 未被JSON包装
        Assert.assertEquals("true", get("/index/v1/ret/true"));
    }
}
