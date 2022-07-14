package bg.softuni.computer_store.model.validation.email;


import bg.softuni.computer_store.repository.UserRepository;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UniqueUserEmailValidator implements ConstraintValidator<UniqueUserEmail, String> {
    private UserRepository userRepository;

    public UniqueUserEmailValidator(UserRepository userRepository) {
        this.userRepository = userRepository;

    }

    //    @Override
//    public void initialize(UniqueUserName constraintAnnotation) {
//        ConstraintValidator.super.initialize(constraintAnnotation);
//    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (this.userRepository.findByUsername(value).isPresent()
                || this.userRepository.findByEmail(value).isPresent()) {
            return false;
        }

        return true;

    }
}
