package work.bottle.demo.controller.v1;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import work.bottle.demo.HotelBaseServiceStartup;

import javax.annotation.Resource;
import java.nio.charset.Charset;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = HotelBaseServiceStartup.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class IndexCustomForceControllerTest {
    private static final Logger logger = LoggerFactory.getLogger(IndexCustomForceControllerTest.class);

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
        Assert.assertEquals("返回字符串测试(/index/v1/ret/str), 结果异常", "{\"code\":0,\"msg\":\"\",\"data\":\"hello world\"}", contentAsString);
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
        Assert.assertEquals("返回中文字符串测试(" + uri + "), 结果异常", "{\"code\":0,\"msg\":\"\",\"data\":\"你好, 朋友\"}", contentAsString);
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
        Assert.assertEquals("返回布尔值true测试(GET " + uri + "), 结果异常", "{\"code\":0,\"msg\":\"\",\"data\":true}", contentAsString);
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
        Assert.assertEquals("返回布尔值false测试(GET " + uri + "), 结果异常", "{\"code\":0,\"msg\":\"\",\"data\":false}", contentAsString);
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
        Assert.assertEquals("返回int<100>测试(GET " + uri + "), 结果异常", "{\"code\":0,\"msg\":\"\",\"data\":100}", contentAsString);
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
        Assert.assertEquals("返回Integer<200>测试(GET " + uri + "), 结果异常", "{\"code\":0,\"msg\":\"\",\"data\":200}", contentAsString);
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
        Assert.assertEquals("返回long<1000L>测试(GET " + uri + "), 结果异常", "{\"code\":0,\"msg\":\"\",\"data\":1000}", contentAsString);
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
        Assert.assertEquals("返回Long<100L>测试(GET " + uri + "), 结果异常", "{\"code\":0,\"msg\":\"\",\"data\":100}", contentAsString);
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
        Assert.assertEquals("返回float<3.14f>测试(GET " + uri + "), 结果异常", "{\"code\":0,\"msg\":\"\",\"data\":3.14}", contentAsString);
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
        Assert.assertEquals("返回double<3.141592654d>测试(GET " + uri + "), 结果异常", "{\"code\":0,\"msg\":\"\",\"data\":3.141592654}", contentAsString);
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
        Assert.assertEquals("返回Float<3.14f>测试(GET " + uri + "), 结果异常", "{\"code\":0,\"msg\":\"\",\"data\":3.14}", contentAsString);
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
        Assert.assertEquals("返回Double<3.141592654d>测试(GET " + uri + "), 结果异常", "{\"code\":0,\"msg\":\"\",\"data\":3.141592654}", contentAsString);
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
        Assert.assertEquals("返回Char<\"a\">测试(GET " + uri + "), 结果异常", "{\"code\":0,\"msg\":\"\",\"data\":\"a\"}", contentAsString);
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
        Assert.assertEquals("返回Byte<105(0x69)>测试(GET " + uri + "), 结果异常", "{\"code\":0,\"msg\":\"\",\"data\":105}", contentAsString);
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
        Assert.assertEquals("返回Byte[]<{0x69, 0x70}(ip)>(GET " + uri + "), 结果异常", "{\"code\":0,\"msg\":\"\",\"data\":\"aXA=\"}", contentAsString);
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
        Assert.assertEquals("返回Object<null>测试(GET " + uri + "), 结果异常", "{\"code\":0,\"msg\":\"\",\"data\":null}", contentAsString);
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
        Assert.assertEquals("返回int[]<[11,22,33]>测试(GET " + uri + "), 结果异常", "{\"code\":0,\"msg\":\"\",\"data\":[11,22,33]}", contentAsString);
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
        Assert.assertEquals("返回Object<String>测试(GET " + uri + "), 结果异常", "{\"code\":0,\"msg\":\"\",\"data\":\"" + s + "\"}", contentAsString);
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
        Assert.assertEquals("返回状态不正确", 200, response.getStatus());
        String contentAsString = response.getContentAsString(Charset.forName("UTF-8"));
        logger.info("返回Object<new Object>测试(GET {}): {}", uri, contentAsString);
        Assert.assertEquals("返回Object<new Object>测试(GET " + uri + "), 结果异常", "{\"code\":0,\"msg\":\"\",\"data\":{}}", contentAsString);
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
        Assert.assertEquals("返回Exception<1 / 0>测试(GET " + uri + "), 结果异常", "{\"code\":500,\"data\":{\"timestamp\":1665375360653,\"path\":\"/index/v1/err/exception\"},\"msg\":\"Internal Server Error\"}", contentAsString);
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
        Assert.assertEquals("返回OperationException<10100>测试(GET " + uri + "), 结果异常", "{\"code\":10100,\"msg\":\"Operation Exception\",\"data\":null}", contentAsString);
    }
}
