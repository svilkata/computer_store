package bg.softuni.computerStore.exception;

//import org.springframework.http.HttpStatus;
//import org.springframework.web.bind.annotation.ResponseStatus;

//@ResponseStatus(value= HttpStatus.NOT_FOUND, reason="No such Item")  // 404
public class ItemNotFoundException extends RuntimeException {
    private ItemErrorInfo item;

    private ItemNotFoundException(String message) {
        super(message);
    }

    public ItemNotFoundException(String message, Long itemId) {
        ItemNotFoundException itemNotFoundException = new ItemNotFoundException(message);
        this.item = new ItemErrorInfo(itemId, itemNotFoundException);
    }

    public ItemErrorInfo getItem() {
        return item;
    }
}
