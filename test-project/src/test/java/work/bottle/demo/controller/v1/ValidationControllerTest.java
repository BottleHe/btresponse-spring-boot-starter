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
 * TC-004/005/008/009: 参数校验异常与请求体反序列化.
 * 字段级校验(422), 类级校验ObjectError(422), @Validated参数与手动ValidationException(415), JSON请求体(200).
 */
@SpringBootTest(classes = HotelBaseServiceStartup.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class ValidationControllerTest {
    private static final Logger logger = LoggerFactory.getLogger(ValidationControllerTest.class);

    @Resource
    private MockMvc mockMvc;

    private DocumentContext perform(MockHttpServletRequestBuilder request, String uri, int expectedStatus) throws Exception {
        MvcResult mvcResult = mockMvc.perform(request).andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        String body = response.getContentAsString(Charset.forName("UTF-8"));
        logger.info("{}: {} {}", uri, response.getStatus(), body);
        Assert.assertEquals("http status异常(" + uri + ")", expectedStatus, response.getStatus());
        return JsonPath.parse(body);
    }

    @Test
    public void testFieldLevelValidation() throws Exception {
        // TC-004: 字段级校验失败 -> 422, message为字段校验消息(FieldError路径)
        DocumentContext body = perform(MockMvcRequestBuilders.post("/index/v1/valid/body")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"page\":0,\"size\":10}"), "/index/v1/valid/body", 422);
        Assert.assertEquals(Boolean.FALSE, body.read("$.success", Boolean.class));
        Assert.assertEquals(422, body.read("$.code", Integer.class).intValue());
        Assert.assertEquals("page必须大于等于1", body.read("$.message", String.class));
    }

    @Test
    public void testClassLevelValidation() throws Exception {
        // TC-005: 类级校验失败产生ObjectError(非FieldError), 不应触发ClassCastException导致500
        DocumentContext body = perform(MockMvcRequestBuilders.post("/index/v1/valid/class-level")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"start\":10,\"end\":1}"), "/index/v1/valid/class-level", 422);
        Assert.assertEquals(422, body.read("$.code", Integer.class).intValue());
        Assert.assertEquals("起始值不能大于结束值", body.read("$.message", String.class));
    }

    @Test
    public void testValidatedQueryParam() throws Exception {
        // @Validated + @RequestParam约束失败 -> ConstraintViolationException(ValidationException子类) -> 415
        DocumentContext body = perform(MockMvcRequestBuilders.get("/index/v1/valid/query?page=-1"),
                "/index/v1/valid/query?page=-1", 415);
        Assert.assertEquals(415, body.read("$.code", Integer.class).intValue());
    }

    @Test
    public void testManualValidationException() throws Exception {
        // TC-008: 手动抛出ValidationException -> 415, message透传
        DocumentContext body = perform(MockMvcRequestBuilders.get("/index/v1/valid/manual"),
                "/index/v1/valid/manual", 415);
        Assert.assertEquals(415, body.read("$.code", Integer.class).intValue());
        Assert.assertEquals("手动触发的校验失败", body.read("$.message", String.class));
    }

    @Test
    public void testLoginJsonBody() throws Exception {
        // TC-009: JSON请求体反序列化正常(Bt转换器canRead=false不影响默认Jackson读取), 响应包装
        DocumentContext body = perform(MockMvcRequestBuilders.post("/index/v1/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"mobile\":\"18117777777\",\"code\":\"123456\"}"), "/index/v1/login", 200);
        Assert.assertEquals(Boolean.TRUE, body.read("$.success", Boolean.class));
        Assert.assertEquals(0, body.read("$.code", Integer.class).intValue());
        Assert.assertEquals("18117777777", body.read("$.data.mobile", String.class));
        Assert.assertEquals("hhhhhhhhhhh", body.read("$.data.authorizationKey", String.class));
    }
}
