package work.bottle.demo.form;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class RangeConsistentValidator implements ConstraintValidator<RangeConsistent, RangeQuery> {

    @Override
    public boolean isValid(RangeQuery value, ConstraintValidatorContext context) {
        if (null == value || null == value.getStart() || null == value.getEnd()) {
            return true;
        }
        return value.getStart() <= value.getEnd();
    }
}
