package work.bottle.demo.controller.v1;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import javax.annotation.Resource;
import java.nio.charset.Charset;

@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
public class MethodIgnoreControllerTest {
    private static final Logger logger = LoggerFactory.getLogger(MethodIgnoreControllerTest.class);

    @Resource
    private MockMvc mockMvc;

    @Test
    public void testGetAccount() throws Exception {
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder =
                MockMvcRequestBuilders.get("/method/ignore/v1/account");
        ResultActions perform = mockMvc.perform(mockHttpServletRequestBuilder);

        MvcResult result = perform.andReturn();
        MockHttpServletResponse response = result.getResponse();
        logger.info("status: {}", response.getStatus());
        logger.info("val: {}", response.getContentAsString(Charset.forName("UTF-8")));
    }

    @Test
    public void testGetStr() throws Exception {
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder =
                MockMvcRequestBuilders.get("/method/ignore/v1/str");
        ResultActions perform = mockMvc.perform(mockHttpServletRequestBuilder);

        MvcResult result = perform.andReturn();
        MockHttpServletResponse response = result.getResponse();
        logger.info("status: {}", response.getStatus());
        logger.info("val: {}", response.getContentAsString(Charset.forName("UTF-8")));

    }

    @Test
    public void testGetByteArr() throws Exception {
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder =
                MockMvcRequestBuilders.get("/method/ignore/v1/byte-arr");
        ResultActions perform = mockMvc.perform(mockHttpServletRequestBuilder);
        MvcResult result = perform.andReturn();
        MockHttpServletResponse response = result.getResponse();
        logger.info("status: {}", response.getStatus());
        logger.info("val: {}", response.getContentAsString(Charset.forName("UTF-8")));

    }
}
