package work.bottle.demo.controller.v1;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import work.bottle.plugin.annotation.Ignore;

import java.util.HashMap;
import java.util.Map;

@Ignore
@RestController
@RequestMapping("/ignore/v1")
public class IgnoreController {

    @RequestMapping("/map")
    public Map<String, String> retMap() {
        HashMap<String, String> stringStringHashMap = new HashMap<>();
        stringStringHashMap.put("hello", "world");
        stringStringHashMap.put("bottle", "haha");

        return stringStringHashMap;
    }

}
