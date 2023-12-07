package ru.yandex.practicum.filmorate.annotation;

import javax.validation.*;
import java.time.LocalDate;

public class MinimumDateValidator implements ConstraintValidator<MinimumDate, LocalDate> {
    private LocalDate earliestDate;

    @Override
    public void initialize(MinimumDate constraintAnnotation) {
        earliestDate = LocalDate.parse(constraintAnnotation.value());
    }

    @Override
    public boolean isValid(LocalDate value, ConstraintValidatorContext context) {
        return value == null || !value.isBefore(earliestDate);
    }
}
