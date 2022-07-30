package bg.softuni.computerStore.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

//@ResponseStatus(value= HttpStatus.NOT_FOUND, reason="No items of that type")  // 404
public class ItemsWithTypeNotFoundException extends RuntimeException {
    private ItemErrorInfo item;

    public ItemsWithTypeNotFoundException(String message) {
        super(message);
        this.item = new ItemErrorInfo(message);
    }

    public ItemErrorInfo getItem() {
        return item;
    }
}
