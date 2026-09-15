package work.bottle.plugin;

import jakarta.servlet.RequestDispatcher;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import work.bottle.plugin.exception.GlobalException;
import work.bottle.plugin.exception.OperationException;
import work.bottle.plugin.exception.global.client.NotFoundException;
import work.bottle.plugin.exception.global.client.UnprocessableException;
import work.bottle.plugin.model.BtResponse;

import java.util.Map;

/**
 * TC-011: BtErrorController直接单元测试(不启动容器).
 * 覆盖ERROR_EXCEPTION本身/cause为业务异常的处理, 以及兜底分支的data净化(不含error/status键).
 */
class BtErrorControllerTest {

    private BtErrorController controller;
    private MockHttpServletRequest request;

    @BeforeEach
    void setUp() {
        controller = new BtErrorController(new BtResponseFactory(), new DefaultErrorAttributes(),
                new ErrorProperties(), new BtResponseProperties());
        request = new MockHttpServletRequest();
        request.setAttribute(RequestDispatcher.ERROR_STATUS_CODE, 500);
        request.setAttribute(RequestDispatcher.ERROR_REQUEST_URI, "/test/uri");
    }

    @Test
    void testOperationExceptionAsErrorException() {
        // 异常本身是OperationException -> 按业务异常处理(修复点: 原实现只看cause)
        OperationException exception = new OperationException(10100, "Operation Exception");
        request.setAttribute(RequestDispatcher.ERROR_EXCEPTION, exception);
        ResponseEntity<BtResponse> entity = Assertions.assertDoesNotThrow(() -> controller.error(request));
        Assertions.assertEquals(200, entity.getStatusCode().value());
        Assertions.assertFalse(entity.getBody().isSuccess());
        Assertions.assertEquals(10100, entity.getBody().getCode());
        Assertions.assertEquals("Operation Exception", entity.getBody().getMessage());
    }

    @Test
    void testOperationExceptionAsCause() {
        // cause是OperationException(容器/过滤器包装场景)
        OperationException operationException = new OperationException(20200, "wrapped", null);
        request.setAttribute(RequestDispatcher.ERROR_EXCEPTION, new RuntimeException(operationException));
        ResponseEntity<BtResponse> entity = Assertions.assertDoesNotThrow(() -> controller.error(request));
        Assertions.assertEquals(200, entity.getStatusCode().value());
        Assertions.assertEquals(20200, entity.getBody().getCode());
    }

    @Test
    void testGlobalExceptionAsErrorException() {
        // 异常本身是GlobalException -> code作为http status(修复点: 原实现不处理GlobalException)
        GlobalException exception = new NotFoundException("访问内容不存在");
        request.setAttribute(RequestDispatcher.ERROR_EXCEPTION, exception);
        ResponseEntity<BtResponse> entity = Assertions.assertDoesNotThrow(() -> controller.error(request));
        Assertions.assertEquals(404, entity.getStatusCode().value());
        Assertions.assertEquals(404, entity.getBody().getCode());
        Assertions.assertEquals("访问内容不存在", entity.getBody().getMessage());
    }

    @Test
    void testGlobalExceptionAsCause() {
        GlobalException globalException = new UnprocessableException("不能处理");
        request.setAttribute(RequestDispatcher.ERROR_EXCEPTION, new RuntimeException(globalException));
        ResponseEntity<BtResponse> entity = Assertions.assertDoesNotThrow(() -> controller.error(request));
        Assertions.assertEquals(422, entity.getStatusCode().value());
        Assertions.assertEquals(422, entity.getBody().getCode());
    }

    @Test
    void testFallbackPurifiesData() {
        // 兜底分支: data中不应包含error/status键(修复点: 原实现依赖构造后的惰性变异)
        request.setAttribute(RequestDispatcher.ERROR_EXCEPTION, new RuntimeException("boom"));
        ResponseEntity<BtResponse> entity = Assertions.assertDoesNotThrow(() -> controller.error(request));
        Assertions.assertEquals(500, entity.getStatusCode().value());
        BtResponse body = entity.getBody();
        Assertions.assertEquals(500, body.getCode());
        Assertions.assertEquals("Internal Server Error", body.getMessage());
        Map<?, ?> data = (Map<?, ?>) body.getData();
        Assertions.assertFalse(data.containsKey("error"), "data不应包含error键");
        Assertions.assertFalse(data.containsKey("status"), "data不应包含status键");
        Assertions.assertEquals("/test/uri", data.get("path"));
    }

    @Test
    void testNoExceptionFallback() {
        request.setAttribute(RequestDispatcher.ERROR_EXCEPTION, null);
        ResponseEntity<BtResponse> entity = Assertions.assertDoesNotThrow(() -> controller.error(request));
        Assertions.assertEquals(500, entity.getStatusCode().value());
        Assertions.assertEquals("Internal Server Error", entity.getBody().getMessage());
    }
}
