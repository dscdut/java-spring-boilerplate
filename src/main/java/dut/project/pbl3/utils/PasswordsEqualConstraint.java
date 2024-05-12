package dut.project.pbl3.utils;


import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = PasswordValidator.class)
public @interface PasswordsEqualConstraint {
    String message();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

