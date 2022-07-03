package work.bottle.demo.controller.v1;

import org.springframework.web.bind.annotation.*;
import work.bottle.demo.model.EmployeeAuth;
import work.bottle.demo.model.EmployeeMobileLoginData;
import work.bottle.demo.model.VerificationData;
import work.bottle.plugin.annotation.Ignore;
import work.bottle.plugin.exception.OperationException;

import java.util.concurrent.ThreadLocalRandom;

@RestController
@RequestMapping("/index/v1")
public class IndexController {

    @GetMapping(value = "/ret/str")
    public String retStr() {
        return "hello world";
    }

    @GetMapping(value = "/ret/str/cn")
    public String retStrCn() {
        return "你好, 朋友";
    }

    @GetMapping("/ret/true")
    public Boolean retTrue() {
        return true;
    }

    @GetMapping("/ret/false")
    public Boolean retFalse() {
        return false;
    }

    @GetMapping("/ret/int")
    public int retInt() {
        return 100;
    }

    @GetMapping("/ret/integer")
    public Integer retInteger() {
        return 200;
    }

    @GetMapping("/ret/long")
    public long retLong() {
        return 1000L;
    }

    @GetMapping("/ret/longp")
    public Long retLongP() {
        return 100L;
    }

    @GetMapping("/ret/float")
    public float retFloat() {
        return 3.14f;
    }

    @GetMapping("/ret/double")
    public double retDouble() {
        return 3.141592654d;
    }

    @GetMapping("/ret/floatp")
    public Float retFloatP() {
        return 3.14f;
    }

    @GetMapping("/ret/doublep")
    public Double retDoubleP() {
        return 3.141592654d;
    }

    @GetMapping("/ret/char")
    public char retChar() {
        return 'a';
    }

    @GetMapping("/ret/byte")
    public byte retByte() {
        return 0x69;
    }

    @GetMapping("/ret/byteArr")
    public byte[] retByteArr() {
        return new byte[]{0x69, 0x70};
    }

    @GetMapping("/ret/null")
    @Ignore
    public Object retNull() {
        return null;
    }

    @GetMapping("/ret/intarr")
    public int[] retIntArr() {
        return new int[]{11, 22, 33};
    }

    @GetMapping("/ret/obj")
    public Object pathVariable(@RequestParam(value = "s", required = false) String s) {
        return s;
    }

    @GetMapping("/ret/nobj")
    public Object nobj() {
        return new Object();
    }

    @GetMapping("/err/exception")
    public Object errException() {
        return 1 / 0;
    }


    @GetMapping("/err/opt/exception")
    public Object operationException() {
        if (true) {
            throw new OperationException(10100, "Operation Exception");
        }
        return "operation exception";
    }

    @PostMapping("/login")
    public EmployeeAuth doLogin(@RequestBody(required = false) EmployeeMobileLoginData data) {
        EmployeeAuth employeeAuth = new EmployeeAuth();

        employeeAuth.setAuthorizationKey("hhhhhhhhhhh");
        employeeAuth.setUsername("bottle");
        employeeAuth.setAuthTimestamp((int)(System.currentTimeMillis() / 1000));
        employeeAuth.setMobile("18117777777");
        return employeeAuth;
    }

    @GetMapping("/code/send")
    public VerificationData sendSmsVerificationData() {
        VerificationData verificationData = new VerificationData();
        int i = ThreadLocalRandom.current().nextInt(800, 999999);
        verificationData.setCode(String.format("%06d", i));
        verificationData.setExpireTimestamp((int)(System.currentTimeMillis() / 1000) + 300); // 5分钟过期
        verificationData.setExpireTimestamp((int)(System.currentTimeMillis() / 1000) + 60); // 1分钟后支持重发
        return verificationData;
    }
}
