package io.coolexplorer.auth.annotations.validators;

import io.coolexplorer.auth.annotations.ClassFieldConstraint;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class ClassFieldValidator implements ConstraintValidator<ClassFieldConstraint, String> {
    private Map<String, String> classFields = new HashMap<>();

    @Override
    public void initialize(ClassFieldConstraint constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
        createClassFieldCollection(constraintAnnotation);
    }

    @Override
    public boolean isValid(String field, ConstraintValidatorContext constraintValidatorContext) {
        if (StringUtils.isNotEmpty(field)) {
            return classFields.containsKey(field);
        }

        return true;
    }

    private void createClassFieldCollection(ClassFieldConstraint constraintAnnotation) {
        Field[] fields = constraintAnnotation.object().getDeclaredFields();
        Arrays.asList(fields).forEach(field -> classFields.put(field.getName(), field.getName()));
    }
}
