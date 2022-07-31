package bg.softuni.computerStore.web.errorHandling;

import bg.softuni.computerStore.exception.BasketIdForbiddenException;
import bg.softuni.computerStore.exception.ObjectIdNotANumberException;
import bg.softuni.computerStore.exception.ItemNotFoundException;
import bg.softuni.computerStore.exception.ItemsWithTypeNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler
    public ModelAndView handleDbExceptions(ItemNotFoundException e) {
        ModelAndView modelAndView = new ModelAndView("errors/item-not-found");
        modelAndView.addObject("item", e.getItem());
        modelAndView.setStatus(HttpStatus.NOT_FOUND);

        return modelAndView;
    }

    @ExceptionHandler
    public ModelAndView handleDbExceptions(ItemsWithTypeNotFoundException e) {
        ModelAndView modelAndView = new ModelAndView("errors/item-not-found");
        modelAndView.addObject("item", e.getItem());
        modelAndView.setStatus(HttpStatus.NOT_FOUND);

        return modelAndView;
    }

//    @ExceptionHandler(value = ItemIdNotANumberException.class)
    @ExceptionHandler(value = ObjectIdNotANumberException.class)
    public ModelAndView handleDbExceptions(ObjectIdNotANumberException e) {
        ModelAndView modelAndView = new ModelAndView("errors/item-not-found");
        modelAndView.addObject("item", e.getItem());
        modelAndView.setStatus(HttpStatus.NOT_FOUND);

        return modelAndView;
    }

    @ExceptionHandler(value = {BasketIdForbiddenException.class})
    public ModelAndView handleDbExceptions(BasketIdForbiddenException e) {
        ModelAndView modelAndView = new ModelAndView("errors/basket-forbidden");
        modelAndView.addObject("item", e.getItem());
        modelAndView.setStatus(HttpStatus.FORBIDDEN);

        return modelAndView;
    }

}

