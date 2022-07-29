package bg.softuni.computerStore.web.errorHandling;

import bg.softuni.computerStore.exception.ItemNotFoundException;
import bg.softuni.computerStore.exception.ItemsWithTypeNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value = {ItemNotFoundException.class})
    public ModelAndView handleDbExceptions(ItemNotFoundException e) {
        ModelAndView modelAndView = new ModelAndView("errors/item-not-found");
        modelAndView.addObject("item", e.getItem());
        modelAndView.setStatus(HttpStatus.NOT_FOUND);

        return modelAndView;
    }

    @ExceptionHandler(value = {ItemsWithTypeNotFoundException.class})
    public ModelAndView handleDbExceptions(ItemsWithTypeNotFoundException e) {
        ModelAndView modelAndView = new ModelAndView("errors/item-not-found");
        modelAndView.addObject("item", e.getItem());
        modelAndView.setStatus(HttpStatus.NOT_FOUND);

        return modelAndView;
    }


}

