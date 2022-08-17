package bg.softuni.computerStore.model.validation.price;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ValidPriceValidator implements ConstraintValidator<ValidPrice, String> {
    private ValidPrice validPrice;
    private ConstraintValidatorContext context;
    private String value;
    private int violationsCount = 0;

    private static final String SHOULD_NOT_BE_NULL_BLANK_EMPTY = "Price can not be null, blank or empty.";
    private static final String SHOULD_BE_WHOLE_OR_FRACTIONAL_NUMBER = "Price should be a whole or fractional number and not a text.";
    private static final String SHOULD_BE_POSITIVE_NUMBER = "Price should be a positive number.";

    @Override
    public void initialize(ValidPrice constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
        this.validPrice = constraintAnnotation;
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        this.value = value;
        this.context = context;
        this.context.disableDefaultConstraintViolation();

        if (!shouldNotBeNullBlankEmpty()) {
            violationsCount++;
            setMessage(SHOULD_NOT_BE_NULL_BLANK_EMPTY);

        } else {
            if (!shouldBeWholeOrFractionalNumber()) {
                violationsCount++;
                setMessage(SHOULD_BE_WHOLE_OR_FRACTIONAL_NUMBER);
            } else {
                if (!shouldBePositiveNumber()) {
                    violationsCount++;
                    setMessage(SHOULD_BE_POSITIVE_NUMBER);
                }
            }
        }

        if (violationsCount == 0) {
            return true;
        } else {
            violationsCount = 0;
            return false;
        }
    }

    private boolean shouldBePositiveNumber() {
        double checkValue = Double.parseDouble(this.value);
        if (checkValue <= 0) {
            return false;
        }

        return true;
    }

    private boolean shouldBeWholeOrFractionalNumber() {
        try {
            Double.parseDouble(this.value);    //checking if the entered price can be parsed to double
        } catch (Exception e) {
            return false;
        }

        return true;
    }

    private boolean shouldNotBeNullBlankEmpty() {
        if (this.value == null || this.value.isBlank()) {
            return false;
        }

        return true;
    }

    private void setMessage(String message) {
        this.context.buildConstraintViolationWithTemplate(message).addConstraintViolation();
    }
}
