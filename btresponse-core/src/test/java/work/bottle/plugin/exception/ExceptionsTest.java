package work.bottle.plugin.exception;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import work.bottle.plugin.exception.global.GlobalError;

import java.lang.reflect.Constructor;
import java.util.Map;

/**
 * TC-063/064: 异常构造器矩阵.
 * 28个预定义GlobalException的默认构造(code/message与注册表一致), 以及OperationException全部构造器.
 */
class ExceptionsTest {

    @Test
    void testAllPredefinedExceptionsDefaultConstructor() throws Exception {
        for (Map.Entry<Integer, Class<? extends GlobalException>> entry : GlobalError.getDefaultExceptionMap().entrySet()) {
            int code = entry.getKey();
            Class<? extends GlobalException> clazz = entry.getValue();
            GlobalException instance = clazz.getDeclaredConstructor().newInstance();
            Assertions.assertEquals(code, instance.getCode(), clazz.getSimpleName() + "默认code不一致");
            Assertions.assertNotNull(instance.getMessage(), clazz.getSimpleName() + "默认message不应为空");
        }
    }

    @Test
    void testAllPredefinedExceptionsWithCustomArgs() throws Exception {
        IllegalArgumentException cause = new IllegalArgumentException("cause");
        for (Map.Entry<Integer, Class<? extends GlobalException>> entry : GlobalError.getDefaultExceptionMap().entrySet()) {
            Class<? extends GlobalException> clazz = entry.getValue();
            String name = clazz.getSimpleName();
            // (String message)
            GlobalException byMessage = clazz.getDeclaredConstructor(String.class).newInstance("m-" + name);
            Assertions.assertEquals("m-" + name, byMessage.getMessage());
            Assertions.assertNull(byMessage.getData());
            // (String message, Object data)
            GlobalException byData = clazz.getDeclaredConstructor(String.class, Object.class).newInstance("m2", "d2");
            Assertions.assertEquals("d2", byData.getData());
            // (String message, Object data, Throwable t)
            GlobalException byCause = clazz.getDeclaredConstructor(String.class, Object.class, Throwable.class)
                    .newInstance("m3", "d3", cause);
            Assertions.assertSame(cause, byCause.getCause());
        }
    }

    @Test
    void testOperationExceptionConstructors() {
        // (int, String)
        OperationException byMessage = new OperationException(10100, "msg");
        Assertions.assertEquals(10100, byMessage.getCode());
        Assertions.assertEquals("msg", byMessage.getMessage());
        Assertions.assertNull(byMessage.getData());
        Assertions.assertNull(byMessage.getCause());

        // (int, String, Object)
        OperationException byData = new OperationException(10100, "msg", "data");
        Assertions.assertEquals("data", byData.getData());

        // (int, String, Object, Throwable) —— 新增构造器
        IllegalStateException cause = new IllegalStateException("root");
        OperationException byCause = new OperationException(10100, "msg", "data", cause);
        Assertions.assertEquals(10100, byCause.getCode());
        Assertions.assertEquals("msg", byCause.getMessage());
        Assertions.assertEquals("data", byCause.getData());
        Assertions.assertSame(cause, byCause.getCause());
    }

    @Test
    void testGlobalExceptionConstructors() {
        IllegalStateException cause = new IllegalStateException("root");
        GlobalException exception = new GlobalException(409, "conflict", "data", cause);
        Assertions.assertEquals(409, exception.getCode());
        Assertions.assertEquals("conflict", exception.getMessage());
        Assertions.assertEquals("data", exception.getData());
        Assertions.assertSame(cause, exception.getCause());
    }
}
