package bg.softuni.computerStore.model.validation.price;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Constraint(validatedBy = ValidPriceValidator.class)
public @interface ValidPrice {
    String message() default "Some message";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    //If we are using fields in the annotation
    boolean shouldNotBeNullBlankEmpty() default true;

    boolean shouldBeWholeOrFractionalNumber() default true;

    boolean shouldBePositiveNumber() default true;
}
