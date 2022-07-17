package bg.softuni.computerStore.model.validation.emailUsername;

import bg.softuni.computerStore.repository.users.UserRepository;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UniqueUserEmailValidator implements ConstraintValidator<UniqueUserEmail, String> {
    private UserRepository userRepository;

    public UniqueUserEmailValidator(UserRepository userRepository) {
        this.userRepository = userRepository;

    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (this.userRepository.findByUsername(value).isPresent()
                || this.userRepository.findByEmail(value).isPresent()) {
            return false;
        }

        return true;
    }
}
