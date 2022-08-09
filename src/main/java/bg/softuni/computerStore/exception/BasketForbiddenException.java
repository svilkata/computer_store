package bg.softuni.computerStore.exception;

public class BasketForbiddenException extends RuntimeException  {
    private ItemErrorInfo item;

    public BasketForbiddenException(String message) {
        super(message);
        this.item = new ItemErrorInfo(message);
    }

    public ItemErrorInfo getItem() {
        return item;
    }
}
