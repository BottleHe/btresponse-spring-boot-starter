package work.bottle.demo.form;

import jakarta.validation.constraints.NotNull;

/**
 * 带类级约束的表单, 校验失败时BindingResult中是ObjectError而非FieldError.
 */
@RangeConsistent
public class RangeQuery {

    @NotNull(message = "start不能为空")
    private Integer start;

    @NotNull(message = "end不能为空")
    private Integer end;

    public Integer getStart() {
        return start;
    }

    public void setStart(Integer start) {
        this.start = start;
    }

    public Integer getEnd() {
        return end;
    }

    public void setEnd(Integer end) {
        this.end = end;
    }
}
