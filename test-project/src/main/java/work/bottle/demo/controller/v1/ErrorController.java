package work.bottle.demo.controller.v1;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import work.bottle.demo.model.SimpleList;
import work.bottle.demo.service.ErrorService;
import work.bottle.plugin.exception.global.client.OutOfBoundsException;
import work.bottle.plugin.exception.global.client.UniquenessException;
import work.bottle.plugin.exception.global.server.InsufficientException;

@RestController
@RequestMapping("/error/v1")
public class ErrorController {

    private final ErrorService errorService;

    public ErrorController(ErrorService errorService) {
        this.errorService = errorService;
    }

    @GetMapping("/t1")
    public SimpleList<String> test01(@RequestParam("n") int n) throws UniquenessException, OutOfBoundsException, InsufficientException {
        return errorService.test(n);
    }
}
