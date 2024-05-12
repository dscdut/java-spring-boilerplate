package dut.project.pbl3.utils;

import dut.project.pbl3.dto.user.CreateUserDto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordValidator implements
        ConstraintValidator<PasswordsEqualConstraint, Object> {
    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        CreateUserDto userDto = (CreateUserDto) value;
        return userDto.getPassword().equals(userDto.getConfirmPassword());
    }
}
