package com.example.bookstore.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.lang.reflect.Field;

public class FieldMatchValidator
        implements ConstraintValidator<FieldMatch, Object> {

    private String first;
    private String second;

    @Override
    public void initialize(FieldMatch constraintAnnotation) {
        first = constraintAnnotation.first();
        second = constraintAnnotation.second();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        try {
            Field firstField = value.getClass().getDeclaredField(first);
            Field secondField = value.getClass().getDeclaredField(second);

            firstField.setAccessible(true);
            secondField.setAccessible(true);

            Object firstVal = firstField.get(value);
            Object secondVal = secondField.get(value);

            if (firstVal == null && secondVal == null) {
                return true;
            }

            if (firstVal != null) {
                return firstVal.equals(secondVal);
            }
            return false;

        } catch (NoSuchFieldException | IllegalAccessException e) {
            return false;
        }
    }
}
