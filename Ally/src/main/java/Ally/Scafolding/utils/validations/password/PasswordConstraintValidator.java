package Ally.Scafolding.utils.validations.password;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordConstraintValidator implements ConstraintValidator<ValidPassword, String> {

    @Override
    public void initialize(ValidPassword constraintAnnotation) {
        // Initialization code, if needed
    }

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        if (password == null) {
            return false;
        }

        // Password must be at least 8 characters long
        if (password.length() < 8) {
            return false;
        }

        // Password must contain at least one uppercase letter
        if (!password.chars().anyMatch(Character::isUpperCase)) {
            return false;
        }

        // Password must contain at least one lowercase letter
        if (!password.chars().anyMatch(Character::isLowerCase)) {
            return false;
        }

        // Password must contain at least one digit
        if (!password.chars().anyMatch(Character::isDigit)) {
            return false;
        }

        // Password must contain at least one special character
        String specialCharacters = "!@#$%^&*()-+";
        if (!password.chars().anyMatch(ch -> specialCharacters.indexOf(ch) >= 0)) {
            return false;
        }

        return true;
    }

}
