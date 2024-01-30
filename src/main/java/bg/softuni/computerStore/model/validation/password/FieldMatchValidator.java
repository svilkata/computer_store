package bg.softuni.computerStore.model.validation.password;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class FieldMatchValidator implements ConstraintValidator<FieldMatch, Object> {
    private String first;
    private String second;
    private String message;

    @Override
    public void initialize(FieldMatch constraintAnnotation) {
        this.first = constraintAnnotation.firstField();
        this.second = constraintAnnotation.secondField();
        this.message = constraintAnnotation.message();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        //In our case Object value will be the UserRegisterDTO
        BeanWrapper beanWrapper = PropertyAccessorFactory.forBeanPropertyAccess(value);
        Object firstValue = beanWrapper.getPropertyValue(this.first);
        Object secondValue = beanWrapper.getPropertyValue(this.second);

        boolean valid;

        if (firstValue == null) { //if password from html form is null
            //if first is null and second(confirmPassword) is null, then do NOT throw exception
            //if first is null and second is NOT null, then DO throw exception
            valid = secondValue == null;
        } else {
            valid = firstValue.equals(secondValue);  //if password and confirmPassword are equal
        }

        if (!valid) {  //when passwords are not the same, return errorHandling message
            context
                    .buildConstraintViolationWithTemplate(message)  //the errorHandling message from @FieldMatch used on UserRegisterDto
                    .addPropertyNode(this.second) //set the error message on the secondValue from UserRegisterDto
                    .addConstraintViolation()
                    .disableDefaultConstraintViolation();  // without default message when error
        }

        return valid;
    }
}
