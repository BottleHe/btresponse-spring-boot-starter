package work.bottle.demo.controller.v1;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import work.bottle.demo.model.CustomResponse;
import work.bottle.demo.model.EmployeeAuth;
import work.bottle.demo.model.EmployeeMobileLoginData;
import work.bottle.demo.model.VerificationData;
import work.bottle.plugin.exception.OperationException;
import work.bottle.plugin.exception.global.client.ConflictException;
import work.bottle.plugin.exception.global.client.InvalidPasswordException;
import work.bottle.plugin.exception.global.client.NotFoundException;
import work.bottle.plugin.exception.global.client.UnknownIdentityException;
import work.bottle.plugin.exception.global.server.ConfigurationException;
import work.bottle.plugin.exception.global.server.InsufficientException;
import work.bottle.plugin.model.BtResponse;

import java.util.HashMap;
import java.util.Map;
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
        // return "heha";
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
        verificationData.setNextSentTimestamp((int)(System.currentTimeMillis() / 1000) + 60); // 1分钟后支持重发
        return verificationData;
    }

    @GetMapping("/e/s/404")
    public String e404() throws NotFoundException {
        if (true) throw NotFoundException.Default;
        return "done";
    }

    @GetMapping("/e/s/506")
    public String e506() throws ConfigurationException {
        if (true) throw ConfigurationException.Default;
        return "done";
    }

    @GetMapping("/e/s/461")
    public String e461() throws UnknownIdentityException {
        if (true) throw UnknownIdentityException.Default;
        return "done";
    }

    @GetMapping("/e/s/561")
    public String e561() throws InsufficientException {
        if (true) throw InsufficientException.Default;
        return "done";
    }

    @GetMapping("/e/s/409-custom")
    public String conflictWithCustomMessage() throws ConflictException {
        HashMap<String, Object> data = new HashMap<>();
        data.put("orderId", 9527L);
        throw new ConflictException("订单状态已变更, 请刷新后重试", data);
    }

    @GetMapping("/ret/btresponse")
    public BtResponse retBtResponse() {
        // 已是包装类型, Advice应透传不再二次包装(isInstance分支)
        HashMap<String, Object> data = new HashMap<>();
        data.put("passthrough", true);
        return new BtResponse(false, 1234, data, "预包装返回");
    }

    @GetMapping("/ret/custom-response")
    public CustomResponse retCustomResponse() {
        // 默认工厂: 会被当作data包装; 自定义工厂(CustomerResponseFactory)场景: 透传
        return new CustomResponse(88, "自定义结构", "payload");
    }

    @GetMapping("/ret/void")
    public void retVoid() {
        // 记录基线行为: void返回不参与包装
    }

    @GetMapping("/ret/map")
    public Map<String, Object> retMap() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("hello", "world");
        map.put("count", 2);
        return map;
    }

    @GetMapping("/ret/response-entity-string")
    public ResponseEntity<String> retResponseEntityString() {
        // String体走StringHttpMessageConverter, Advice不介入, 应原样返回
        return ResponseEntity.ok("entity-body");
    }

    @GetMapping("/ret/response-entity-bt")
    public ResponseEntity<BtResponse> retResponseEntityBt() {
        // ResponseEntity包装的已是BtResponse: 透传且保留201状态
        return ResponseEntity.status(201).body(new BtResponse(true, 0, "created", ""));
    }
}
