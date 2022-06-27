package work.bottle.demo.controller.v1;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import work.bottle.plugin.annotation.Ignore;
import work.bottle.plugin.exception.OperationException;

@RestController
@RequestMapping("/index/v1")
public class IndexController {

    @GetMapping(value = "/ret/str")
    public String retStr() {
        return "hello world";
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
}
