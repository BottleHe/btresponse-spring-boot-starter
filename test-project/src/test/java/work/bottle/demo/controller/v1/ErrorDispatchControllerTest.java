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
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import work.bottle.demo.HotelBaseServiceStartup;

/**
 * TC-040/041/043/044: 容器级错误分发(404/405/415)与错误属性透传.
 * 这些错误不经过ExceptionHandler, 由error page转发到ErrorController后由Advice统一包装.
 */
@SpringBootTest(classes = HotelBaseServiceStartup.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = {"server.error.include-message=always"})
@AutoConfigureMockMvc
class ErrorDispatchControllerTest {
    private static final Logger logger = LoggerFactory.getLogger(ErrorDispatchControllerTest.class);

    @Resource
    private TestRestTemplate testRestTemplate;

    private DocumentContext perform(String uri, String method, int expectedStatus, HttpEntity<?> entity) {
        ResponseEntity<String> httpResponse;
        if ("POST".equals(method)) {
            httpResponse = testRestTemplate.postForEntity(uri, entity, String.class);
        } else {
            httpResponse = testRestTemplate.getForEntity(uri, String.class);
        }
        logger.info("{} {}: {} {}", method, uri, httpResponse.getStatusCode(), httpResponse.getBody());
        Assert.assertEquals("http status异常(" + uri + ")", expectedStatus, httpResponse.getStatusCode().value());
        return JsonPath.parse(httpResponse.getBody());
    }

    @Test
    public void testNotFoundPath() {
        // TC-040: 未知路径404也走统一错误格式
        DocumentContext body = perform("/not/exist/path", "GET", 404, null);
        Assert.assertEquals(Boolean.FALSE, body.read("$.success", Boolean.class));
        Assert.assertEquals(404, body.read("$.code", Integer.class).intValue());
        Assert.assertEquals("/not/exist/path", body.read("$.data.path", String.class));
    }

    @Test
    public void testMethodNotAllowed() {
        // TC-041: 405方法不允许
        DocumentContext body = perform("/index/v1/ret/str", "POST", 405, null);
        Assert.assertEquals(405, body.read("$.code", Integer.class).intValue());
    }

    @Test
    public void testUnsupportedContentType() {
        // TC-043: 不支持的Content-Type -> 415
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_PLAIN);
        DocumentContext body = perform("/index/v1/login", "POST", 415, new HttpEntity<>("plain", headers));
        Assert.assertEquals(415, body.read("$.code", Integer.class).intValue());
    }

    @Test
    public void testIncludeMessageAlways() {
        // TC-044: include-message=always时, 异常message透传到data.message
        DocumentContext body = perform("/index/v1/err/exception", "GET", 500, null);
        Assert.assertEquals(500, body.read("$.code", Integer.class).intValue());
        Assert.assertEquals("/ by zero", body.read("$.data.message", String.class));
    }
}
