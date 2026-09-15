package work.bottle.demo.controller.v1;

import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import jakarta.validation.constraints.Min;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import work.bottle.demo.form.RangeQuery;
import work.bottle.demo.form.ValidForm;

/**
 * 参数校验场景端点, 覆盖三种校验失败路径:
 * 1. @Valid @RequestBody 字段级失败 -> MethodArgumentNotValidException(BindException子类) -> 422
 * 2. @Valid @RequestBody 类级失败(自定义约束) -> ObjectError -> 422
 * 3. @Validated + @RequestParam 约束失败 -> ConstraintViolationException(ValidationException子类) -> 415
 */
@Validated
@RestController
@RequestMapping("/index/v1/valid")
public class ValidationController {

    @PostMapping("/body")
    public ValidForm body(@Valid @RequestBody ValidForm form) {
        return form;
    }

    @PostMapping("/class-level")
    public RangeQuery classLevel(@Valid @RequestBody RangeQuery query) {
        return query;
    }

    @GetMapping("/query")
    public String query(@RequestParam("page") @Min(value = 1, message = "page必须大于等于1") int page) {
        return "page=" + page;
    }

    @GetMapping("/manual")
    public String manual() {
        throw new ValidationException("手动触发的校验失败");
    }
}
