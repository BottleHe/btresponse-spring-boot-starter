package work.bottle.demo.others;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

@SpringBootTest
public class HttpStatusTest {

    private static final Logger logger = LoggerFactory.getLogger(HttpStatusTest.class);

    @Test
    public void testResolveHttpStatus() {
        int code = 103213;
        HttpStatus httpStatus = HttpStatus.resolve(code);
        logger.info("http status : {}", httpStatus);
    }
}
