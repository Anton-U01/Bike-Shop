package softuni.bg.bikeshop.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND,reason = "Product was not found.")
public class ProductNotFoundException extends RuntimeException{
    private String message;

    public ProductNotFoundException(String message){
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
