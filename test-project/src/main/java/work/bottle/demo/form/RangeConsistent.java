package work.bottle.demo.form;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 类级校验约束: 产生ObjectError(而非FieldError), 用于验证BindException处理器对类级校验错误的兼容性.
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = RangeConsistentValidator.class)
public @interface RangeConsistent {

    String message() default "起始值不能大于结束值";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
