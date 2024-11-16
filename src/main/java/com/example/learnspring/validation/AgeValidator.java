package com.example.learnspring.validation;

import java.time.LocalDate;
import java.time.Period;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class AgeValidator implements ConstraintValidator<ValidAge, LocalDate> {
    private int minAge;

    @Override
    public void initialize(ValidAge constraintAnnotation) {
        this.minAge = constraintAnnotation.min();
    }

    @Override
    public boolean isValid(LocalDate dob, ConstraintValidatorContext context) {
        if (dob == null) {
            return true; // Để null pass qua, nếu cần bắt lỗi null thì dùng @NotNull trên thuộc tính
        }
        return Period.between(dob, LocalDate.now()).getYears() >= minAge;
    }
}
