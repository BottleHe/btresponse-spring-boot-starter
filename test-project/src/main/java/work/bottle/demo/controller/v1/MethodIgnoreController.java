package work.bottle.demo.controller.v1;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import work.bottle.demo.domain.Account;
import work.bottle.plugin.annotation.Ignore;

@RestController
@RequestMapping("/method/ignore/v1")
public class MethodIgnoreController {

    @Ignore
    @GetMapping("/account")
    public Account getAccount() {
        Account account = new Account();
        account.setBalance(1999L);
        account.setId(1888);
        account.setName("BottleHe");
        return account;
    }

    @Ignore
    @GetMapping("/str")
    public String getStr() {
        return "hello world";
    }

    @Ignore
    @GetMapping("/byte-arr")
    public byte[] getByteArr() {
        return new byte[]{
                0x12, 0x15, 0x30, 0x16
        };
    }
}
