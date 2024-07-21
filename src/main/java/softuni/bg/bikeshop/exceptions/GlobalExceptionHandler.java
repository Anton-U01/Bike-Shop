package softuni.bg.bikeshop.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    @ExceptionHandler(ProductNotFoundException.class)
    public ModelAndView handleProductNotFound(ProductNotFoundException e){
        ModelAndView modelAndView = new ModelAndView("object-not-found");
        modelAndView.addObject("errorMessage",e.getMessage());

        return modelAndView;
    }
    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    @ExceptionHandler(UserNotFoundException.class)
    public ModelAndView handleUserNotFound(UserNotFoundException e){
        ModelAndView modelAndView = new ModelAndView("object-not-found");
        modelAndView.addObject("errorMessage",e.getMessage());

        return modelAndView;
    }

}
