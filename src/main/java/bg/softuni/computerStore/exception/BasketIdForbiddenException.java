package bg.softuni.computerStore.exception;

public class BasketIdForbiddenException extends RuntimeException  {
    private ItemErrorInfo item;

    public BasketIdForbiddenException(String message) {
        super(message);
        this.item = new ItemErrorInfo(message);
    }

    public ItemErrorInfo getItem() {
        return item;
    }
}
