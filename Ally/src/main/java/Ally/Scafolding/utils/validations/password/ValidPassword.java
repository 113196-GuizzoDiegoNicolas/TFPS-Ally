package Ally.Scafolding.utils.validations.password;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.passay.PasswordValidator;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.net.Proxy;


import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = PasswordConstraintValidator.class)
@Target({FIELD, ANNOTATION_TYPE})
@Retention(RUNTIME)

public @interface ValidPassword {
    String message() default "Password Invalido";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
