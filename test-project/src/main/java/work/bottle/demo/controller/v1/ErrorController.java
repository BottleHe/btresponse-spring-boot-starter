package work.bottle.demo.controller.v1;

import org.springframework.web.bind.annotation.*;
import work.bottle.demo.model.SimpleList;
import work.bottle.demo.service.ErrorService;
import work.bottle.plugin.annotation.Ignore;
import work.bottle.plugin.exception.GlobalException;
import work.bottle.plugin.exception.global.GlobalError;
import work.bottle.plugin.exception.global.client.OutOfBoundsException;
import work.bottle.plugin.exception.global.client.UniquenessException;
import work.bottle.plugin.exception.global.server.InsufficientException;

import java.util.List;

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

    @GetMapping("/version")
    public int globalErrorVersion() {
        return GlobalError.getVersion();
    }

    @RequestMapping("/info")
    public List<GlobalError.ErrorInfo> globalError() {
        return GlobalError.getErrorInfoList();
    }

    @RequestMapping(value = "/e561", method = {RequestMethod.GET, RequestMethod.HEAD, RequestMethod.DELETE, RequestMethod.PUT, RequestMethod.POST})
    public int e561() throws GlobalException {
        try {
            throw GlobalError.getExceptionClass(561).newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Ignore
    @RequestMapping(value = "/e561/ignore", method = {RequestMethod.GET, RequestMethod.HEAD, RequestMethod.DELETE, RequestMethod.PUT, RequestMethod.POST})
    public int e561Ignore() throws GlobalException {
        try {
            throw GlobalError.getExceptionClass(561).newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
