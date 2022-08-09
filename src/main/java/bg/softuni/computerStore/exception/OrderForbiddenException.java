package bg.softuni.computerStore.exception;

public class OrderForbiddenException extends RuntimeException  {
    private ItemErrorInfo item;

    public OrderForbiddenException(String message) {
        super(message);
        this.item = new ItemErrorInfo(message);
    }

    public ItemErrorInfo getItem() {
        return item;
    }
}
