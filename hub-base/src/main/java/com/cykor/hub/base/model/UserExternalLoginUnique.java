package com.cykor.hub.base.model;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;

import com.cykor.hub.base.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Map;
import java.util.UUID;
import org.springframework.web.servlet.HandlerMapping;


/**
 * Validate that the externalLogin value isn't taken yet.
 */
@Target({ FIELD, METHOD, ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(
        validatedBy = UserExternalLoginUnique.UserExternalLoginUniqueValidator.class
)
public @interface UserExternalLoginUnique {

    String message() default "{exists.user.externalLogin}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    class UserExternalLoginUniqueValidator implements ConstraintValidator<UserExternalLoginUnique, String> {

        private final UserService userService;
        private final HttpServletRequest request;

        public UserExternalLoginUniqueValidator(final UserService userService,
                final HttpServletRequest request) {
            this.userService = userService;
            this.request = request;
        }

        @Override
        public boolean isValid(final String value, final ConstraintValidatorContext cvContext) {
            if (value == null) {
                // no value present
                return true;
            }
            @SuppressWarnings("unchecked") final Map<String, String> pathVariables =
                    ((Map<String, String>)request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE));
            final String currentId = pathVariables.get("id");
            if (currentId != null && value.equalsIgnoreCase(userService.get(UUID.fromString(currentId)).getExternalLogin())) {
                // value hasn't changed
                return true;
            }
            return !userService.externalLoginExists(value);
        }

    }

}
