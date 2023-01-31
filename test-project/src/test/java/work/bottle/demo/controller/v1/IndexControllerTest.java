package work.bottle.demo.controller.v1;

import org.junit.Assert;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.WebApplicationContext;
import work.bottle.demo.HotelBaseServiceStartup;
import work.bottle.demo.model.EmployeeAuth;
import work.bottle.demo.model.EmployeeMobileLoginData;
import work.bottle.demo.model.VerificationData;
import work.bottle.plugin.exception.OperationException;

import javax.annotation.Resource;
import java.nio.charset.Charset;
import java.util.concurrent.ThreadLocalRandom;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = HotelBaseServiceStartup.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class IndexControllerTest {
    private static final Logger logger = LoggerFactory.getLogger(IndexControllerTest.class);

    @Resource
    private MockMvc mockMvc;

//    @Autowired
//    private WebApplicationContext context;


    @Test
    public void testRetStr() throws Exception {
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder =
                MockMvcRequestBuilders.get("/index/v1/ret/str");

        ResultActions perform = mockMvc.perform(mockHttpServletRequestBuilder);
        MvcResult mvcResult = perform.andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();
        logger.info("返回字符串测试(/index/v1/ret/str): {}", contentAsString);
        Assert.assertEquals("返回字符串测试(/index/v1/ret/str), 结果异常", "hello world", contentAsString);
    }

    @Test
    public void testRetStrCn() throws Exception {
        final String uri = "/index/v1/ret/str/cn";
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder =
                MockMvcRequestBuilders.get(uri);
        mockHttpServletRequestBuilder.characterEncoding("UTF-8");

        ResultActions perform = mockMvc.perform(mockHttpServletRequestBuilder);
        MvcResult mvcResult = perform.andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString(Charset.forName("UTF-8"));
        logger.info("返回字符串测试(GET {}): {}", uri, contentAsString);
        Assert.assertEquals("返回中文字符串测试(" + uri + "), 结果异常", "你好, 朋友", contentAsString);
    }

    @Test
    public void testRetTrue() throws Exception {
        final String uri = "/index/v1/ret/true";
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder =
                MockMvcRequestBuilders.get(uri);

        ResultActions perform = mockMvc.perform(mockHttpServletRequestBuilder);
        MvcResult mvcResult = perform.andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();
        logger.info("返回布尔值true测试(GET {}): {}", uri, contentAsString);
        Assert.assertEquals("返回布尔值true测试(GET " + uri + "), 结果异常", "{\"success\":true,\"code\":0,\"data\":true,\"message\":\"\"}", contentAsString);
    }

    @Test
    public void testRetFalse() throws Exception {
        final String uri = "/index/v1/ret/false";
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder =
                MockMvcRequestBuilders.get(uri);

        ResultActions perform = mockMvc.perform(mockHttpServletRequestBuilder);
        MvcResult mvcResult = perform.andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();
        logger.info("返回布尔值false测试(GET {}): {}", uri, contentAsString);
        Assert.assertEquals("返回布尔值false测试(GET " + uri + "), 结果异常", "{\"success\":true,\"code\":0,\"data\":false,\"message\":\"\"}", contentAsString);
    }

    @Test
    public void testRetInt() throws Exception {
        final String uri = "/index/v1/ret/int";
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder =
                MockMvcRequestBuilders.get(uri);

        ResultActions perform = mockMvc.perform(mockHttpServletRequestBuilder);
        MvcResult mvcResult = perform.andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        String contentAsString = response.getContentAsString(Charset.forName("UTF-8"));
        logger.info("返回int<100>测试(GET {}): {}", uri, contentAsString);
        Assert.assertEquals("返回int<100>测试(GET " + uri + "), 结果异常", "{\"success\":true,\"code\":0,\"data\":100,\"message\":\"\"}", contentAsString);
    }

    @Test
    public void testRetInteger() throws Exception {
        final String uri = "/index/v1/ret/integer";
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder =
                MockMvcRequestBuilders.get(uri);

        ResultActions perform = mockMvc.perform(mockHttpServletRequestBuilder);
        MvcResult mvcResult = perform.andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        String contentAsString = response.getContentAsString(Charset.forName("UTF-8"));
        logger.info("返回Integer<200>测试(GET {}): {}", uri, contentAsString);
        Assert.assertEquals("返回Integer<200>测试(GET " + uri + "), 结果异常", "{\"success\":true,\"code\":0,\"data\":200,\"message\":\"\"}", contentAsString);
    }

    @Test
    public void testRetLong() throws Exception {
        final String uri = "/index/v1/ret/long";
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder =
                MockMvcRequestBuilders.get(uri);

        ResultActions perform = mockMvc.perform(mockHttpServletRequestBuilder);
        MvcResult mvcResult = perform.andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        String contentAsString = response.getContentAsString(Charset.forName("UTF-8"));
        logger.info("返回long<1000L>测试(GET {}): {}", uri, contentAsString);
        Assert.assertEquals("返回long<1000L>测试(GET " + uri + "), 结果异常", "{\"success\":true,\"code\":0,\"data\":1000,\"message\":\"\"}", contentAsString);
    }

    @Test
    public void testRetLongP() throws Exception {
        final String uri = "/index/v1/ret/longp";
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder =
                MockMvcRequestBuilders.get(uri);

        ResultActions perform = mockMvc.perform(mockHttpServletRequestBuilder);
        MvcResult mvcResult = perform.andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        String contentAsString = response.getContentAsString(Charset.forName("UTF-8"));
        logger.info("返回Long<100L>测试(GET {}): {}", uri, contentAsString);
        Assert.assertEquals("返回Long<100L>测试(GET " + uri + "), 结果异常", "{\"success\":true,\"code\":0,\"data\":100,\"message\":\"\"}", contentAsString);
    }

    @Test
    public void testRetFloat() throws Exception {
        final String uri = "/index/v1/ret/float";
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder =
                MockMvcRequestBuilders.get(uri);

        ResultActions perform = mockMvc.perform(mockHttpServletRequestBuilder);
        MvcResult mvcResult = perform.andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        String contentAsString = response.getContentAsString(Charset.forName("UTF-8"));
        logger.info("返回float<3.14f>测试(GET {}): {}", uri, contentAsString);
        Assert.assertEquals("返回float<3.14f>测试(GET " + uri + "), 结果异常", "{\"success\":true,\"code\":0,\"data\":3.14,\"message\":\"\"}", contentAsString);
    }

    @Test
    public void testRetDouble() throws Exception {
        final String uri = "/index/v1/ret/double";
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder =
                MockMvcRequestBuilders.get(uri);

        ResultActions perform = mockMvc.perform(mockHttpServletRequestBuilder);
        MvcResult mvcResult = perform.andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        String contentAsString = response.getContentAsString(Charset.forName("UTF-8"));
        logger.info("返回double<3.141592654d>测试(GET {}): {}", uri, contentAsString);
        Assert.assertEquals("返回double<3.141592654d>测试(GET " + uri + "), 结果异常", "{\"success\":true,\"code\":0,\"data\":3.141592654,\"message\":\"\"}", contentAsString);
    }

    @Test
    public void testRetFloatP() throws Exception {
        final String uri = "/index/v1/ret/floatp";
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder =
                MockMvcRequestBuilders.get(uri);

        ResultActions perform = mockMvc.perform(mockHttpServletRequestBuilder);
        MvcResult mvcResult = perform.andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        String contentAsString = response.getContentAsString(Charset.forName("UTF-8"));
        logger.info("返回Float<3.14f>测试(GET {}): {}", uri, contentAsString);
        Assert.assertEquals("返回Float<3.14f>测试(GET " + uri + "), 结果异常", "{\"success\":true,\"code\":0,\"data\":3.14,\"message\":\"\"}", contentAsString);
    }

    @Test
    public void testRetDoubleP() throws Exception {
        final String uri = "/index/v1/ret/doublep";
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder =
                MockMvcRequestBuilders.get(uri);

        ResultActions perform = mockMvc.perform(mockHttpServletRequestBuilder);
        MvcResult mvcResult = perform.andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        String contentAsString = response.getContentAsString(Charset.forName("UTF-8"));
        logger.info("返回Double<3.141592654d>测试(GET {}): {}", uri, contentAsString);
        Assert.assertEquals("返回Double<3.141592654d>测试(GET " + uri + "), 结果异常", "{\"success\":true,\"code\":0,\"data\":3.141592654,\"message\":\"\"}", contentAsString);
    }

    @Test
    public void testRetChar() throws Exception {
        final String uri = "/index/v1/ret/char";
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder =
                MockMvcRequestBuilders.get(uri);
        ResultActions perform = mockMvc.perform(mockHttpServletRequestBuilder);
        MvcResult mvcResult = perform.andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        String contentAsString = response.getContentAsString(Charset.forName("UTF-8"));
        logger.info("返回Char<\"a\">测试(GET {}): {}", uri, contentAsString);
        Assert.assertEquals("返回Char<\"a\">测试(GET " + uri + "), 结果异常", "{\"success\":true,\"code\":0,\"data\":\"a\",\"message\":\"\"}", contentAsString);
    }

    @Test
    public void testRetByte() throws Exception {
        final String uri = "/index/v1/ret/byte";
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder =
                MockMvcRequestBuilders.get(uri);

        ResultActions perform = mockMvc.perform(mockHttpServletRequestBuilder);
        MvcResult mvcResult = perform.andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        String contentAsString = response.getContentAsString(Charset.forName("UTF-8"));
        logger.info("返回Byte<105(0x69)>测试(GET {}): {}", uri, contentAsString);
        Assert.assertEquals("返回Byte<105(0x69)>测试(GET " + uri + "), 结果异常", "{\"success\":true,\"code\":0,\"data\":105,\"message\":\"\"}", contentAsString);
    }

    @Test
    public void testRetByteArr() throws Exception {
        final String uri = "/index/v1/ret/byteArr";
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder =
                MockMvcRequestBuilders.get(uri);

        ResultActions perform = mockMvc.perform(mockHttpServletRequestBuilder);
        MvcResult mvcResult = perform.andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        String contentAsString = response.getContentAsString(Charset.forName("UTF-8"));
        logger.info("返回Byte[]<{0x69, 0x70}(ip)>测试(GET {}): {}", uri, contentAsString);
        Assert.assertEquals("返回Byte[]<{0x69, 0x70}(ip)>(GET " + uri + "), 结果异常", "ip", contentAsString);
    }

    @Test
    public void testRetNull() throws Exception {
        final String uri = "/index/v1/ret/null";
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder =
                MockMvcRequestBuilders.get(uri);

        ResultActions perform = mockMvc.perform(mockHttpServletRequestBuilder);
        MvcResult mvcResult = perform.andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        String contentAsString = response.getContentAsString(Charset.forName("UTF-8"));
        logger.info("返回Object<null>测试(GET {}): {}", uri, contentAsString);
        Assert.assertEquals("返回Object<null>测试(GET " + uri + "), 结果异常", "", contentAsString);
    }

    @Test
    public void testRetIntArr() throws Exception {
        final String uri = "/index/v1/ret/intarr";
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder =
                MockMvcRequestBuilders.get(uri);

        ResultActions perform = mockMvc.perform(mockHttpServletRequestBuilder);
        MvcResult mvcResult = perform.andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        String contentAsString = response.getContentAsString(Charset.forName("UTF-8"));
        logger.info("返回int[]<[11,22,33]>测试(GET {}): {}", uri, contentAsString);
        Assert.assertEquals("返回int[]<[11,22,33]>测试(GET " + uri + "), 结果异常", "{\"success\":true,\"code\":0,\"data\":[11,22,33],\"message\":\"\"}", contentAsString);
    }

    @Test
    public void testPathVariable() throws Exception {
        final String s = "aabbcc";
        final String uri = "/index/v1/ret/obj?s=" + s;
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder =
                MockMvcRequestBuilders.get(uri);

        ResultActions perform = mockMvc.perform(mockHttpServletRequestBuilder);
        MvcResult mvcResult = perform.andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        String contentAsString = response.getContentAsString(Charset.forName("UTF-8"));
        logger.info("返回Object<String>测试(GET {}): {}", uri, contentAsString);
        // Assert.assertEquals("返回Object<String>测试(GET " + uri + "), 结果异常", "{\"success\":true,\"code\":0,\"data\":\"" + s + "\",\"message\":\"\"}", contentAsString);
        Assert.assertEquals("返回Object<String>测试(GET " + uri + "), 结果异常", s, contentAsString);
    }

    @Test
    public void testNobj() throws Exception {
        final String uri = "/index/v1/ret/nobj";
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder =
                MockMvcRequestBuilders.get(uri);

        ResultActions perform = mockMvc.perform(mockHttpServletRequestBuilder);
        MvcResult mvcResult = perform.andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        logger.info("返回Object<new Object>测试(GET {}): HTTP STATUS: {}", uri, response.getStatus());
        Assert.assertEquals("返回状态不正确", 406, response.getStatus());
        // String contentAsString = response.getContentAsString(Charset.forName("UTF-8"));
//            logger.info("返回Object<new Object>测试(GET {}): {}", uri, contentAsString);
        // Assert.assertEquals("返回Object<new Object>测试(GET " + uri + "), 结果异常", "{\"success\":false,\"code\":406,\"data\":{\"timestamp\":1665382833719,\"path\":\"/index/v1/ret/nobj\"},\"message\":\"Not Acceptable\"}", contentAsString);
    }

    @Test
    public void testErrException() throws Exception {
        final String uri = "/index/v1/err/exception";
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder =
                MockMvcRequestBuilders.get(uri);
        ResultActions perform = mockMvc.perform(mockHttpServletRequestBuilder);
        MvcResult mvcResult = perform.andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        String contentAsString = response.getContentAsString(Charset.forName("UTF-8"));
        logger.info("返回Exception<1 / 0>测试(GET {}): {}", uri, contentAsString);
        Assert.assertEquals("返回Exception<1 / 0>测试(GET " + uri + "), 结果异常", "{\"success\":false,\"code\":500,\"data\":{\"timestamp\":1665375360653,\"path\":\"/index/v1/err/exception\"},\"message\":\"Internal Server Error\"}", contentAsString);
    }


    @Test
    public void testOperationException() throws Exception {
        final String uri = "/index/v1/err/opt/exception";
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder =
                MockMvcRequestBuilders.get(uri);
        ResultActions perform = mockMvc.perform(mockHttpServletRequestBuilder);
        MvcResult mvcResult = perform.andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        String contentAsString = response.getContentAsString(Charset.forName("UTF-8"));
        logger.info("返回OperationException<10100>测试(GET {}): {}", uri, contentAsString);
        Assert.assertEquals("返回OperationException<10100>测试(GET " + uri + "), 结果异常", "{\"success\":false,\"code\":10100,\"data\":null,\"message\":\"Operation Exception\"}", contentAsString);
    }

    @Test
    public void testE404Exception() throws Exception {
        final String uri = "/index/v1/e/s/404";
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder =
                MockMvcRequestBuilders.get(uri);
        ResultActions perform = mockMvc.perform(mockHttpServletRequestBuilder);
        MvcResult mvcResult = perform.andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        int status = response.getStatus();
        Assert.assertEquals("not 404", 404, status);
    }


    @Test
    public void testE506Exception() throws Exception {
        final String uri = "/index/v1/e/s/506";
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder =
                MockMvcRequestBuilders.get(uri);
        ResultActions perform = mockMvc.perform(mockHttpServletRequestBuilder);
        MvcResult mvcResult = perform.andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        int status = response.getStatus();
        Assert.assertEquals("not 506", 506, status);
    }

    @Test
    public void testE461Exception() throws Exception {
        final String uri = "/index/v1/e/s/461";
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder =
                MockMvcRequestBuilders.get(uri);
        ResultActions perform = mockMvc.perform(mockHttpServletRequestBuilder);
        MvcResult mvcResult = perform.andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        int status = response.getStatus();
        Assert.assertEquals("not 461", 461, status);
    }

    @Test
    public void testE561Exception() throws Exception {
        final String uri = "/index/v1/e/s/561";
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder =
                MockMvcRequestBuilders.get(uri);
        ResultActions perform = mockMvc.perform(mockHttpServletRequestBuilder);
        MvcResult mvcResult = perform.andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        int status = response.getStatus();
        Assert.assertEquals("not 561", 561, status);
    }
}
