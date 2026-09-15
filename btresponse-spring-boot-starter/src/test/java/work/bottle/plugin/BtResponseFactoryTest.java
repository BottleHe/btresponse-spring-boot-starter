package work.bottle.plugin;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import work.bottle.plugin.model.BtResponse;

/**
 * TC-065: BtResponseFactory生产行为.
 */
class BtResponseFactoryTest {

    private final BtResponseFactory factory = new BtResponseFactory();

    @Test
    void testIsInstance() {
        Assertions.assertTrue(factory.isInstance(new BtResponse()));
        Assertions.assertFalse(factory.isInstance(new Object()));
        Assertions.assertFalse(factory.isInstance(null));
    }

    @Test
    void testProduceResponseSuccessFlag() {
        // code==0 -> success=true
        BtResponse ok = factory.produceResponse(0, "", null);
        Assertions.assertTrue(ok.isSuccess());
        Assertions.assertEquals(0, ok.getCode());

        // code!=0 -> success=false
        BtResponse fail = factory.produceResponse(404, "not found", null);
        Assertions.assertFalse(fail.isSuccess());
        Assertions.assertEquals(404, fail.getCode());
        Assertions.assertEquals("not found", fail.getMessage());

        // 显式success优先
        BtResponse explicit = factory.produceResponse(true, 500, "msg", null);
        Assertions.assertTrue(explicit.isSuccess());
    }

    @Test
    void testProduceResponseEntity() {
        ResponseEntity<BtResponse> entity = factory.produceResponseEntity(false, 422, "msg", null, 422, null);
        Assertions.assertEquals(422, entity.getStatusCode().value());
        Assertions.assertEquals(422, entity.getBody().getCode());
    }
}
