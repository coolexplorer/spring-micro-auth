package io.coolexplorer.auth.annotations;

import io.coolexplorer.auth.annotations.validators.ClassFieldValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = ClassFieldValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ClassFieldConstraint {
    String message() default "Invalid Field Name";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    Class<?> object() default Object.class;
}
