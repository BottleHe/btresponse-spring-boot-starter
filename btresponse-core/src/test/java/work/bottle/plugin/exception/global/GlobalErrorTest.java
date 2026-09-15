package work.bottle.plugin.exception.global;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import work.bottle.plugin.exception.GlobalException;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * TC-060/061/062: GlobalError注册表完整性.
 */
class GlobalErrorTest {

    @Test
    void testRegistrySize() {
        Assertions.assertEquals(28, GlobalError.getDefaultExceptionMap().size());
        Assertions.assertEquals(28, GlobalError.getErrorInfoList().size());
    }

    @Test
    void testClientServerClassification() {
        for (Map.Entry<Integer, Class<? extends GlobalException>> entry : GlobalError.getDefaultExceptionMap().entrySet()) {
            int code = entry.getKey();
            String className = entry.getValue().getName();
            if (code < 500) {
                Assertions.assertTrue(className.contains(".client."), code + "应为client异常: " + className);
            } else {
                Assertions.assertTrue(className.contains(".server."), code + "应为server异常: " + className);
            }
        }
    }

    @Test
    void testGetExceptionClassFallback() {
        // 已注册code返回对应类
        Assertions.assertEquals(GlobalError.getDefaultExceptionMap().get(404), GlobalError.getExceptionClass(404));
        // 未注册code回退到500
        Assertions.assertEquals(GlobalError.getDefaultExceptionMap().get(500), GlobalError.getExceptionClass(99999));
    }

    @Test
    void testErrorInfoListConsistentWithPool() {
        List<GlobalError.ErrorInfo> errorInfoList = GlobalError.getErrorInfoList();
        Set<Integer> poolCodes = GlobalError.getDefaultExceptionMap().keySet();
        Set<Integer> infoCodes = new HashSet<>();
        for (GlobalError.ErrorInfo info : errorInfoList) {
            infoCodes.add(info.code);
            // title与异常类simpleName一致(如Title=NotFound -> NotFoundException)
            String simpleName = GlobalError.getDefaultExceptionMap().get(info.code).getSimpleName();
            Assertions.assertEquals(simpleName, info.title + "Exception",
                    "title与类名不一致: " + info.code);
            Assertions.assertNotNull(info.message);
            Assertions.assertFalse(info.message.isEmpty());
        }
        Assertions.assertEquals(poolCodes, infoCodes, "errorInfoList与EXCEPTION_POOL的code集合应一致");
    }
}
