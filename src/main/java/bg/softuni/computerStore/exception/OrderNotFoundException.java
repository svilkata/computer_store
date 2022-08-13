package bg.softuni.computerStore.exception;

public class OrderNotFoundException extends RuntimeException {
    private ItemErrorInfo item;

    public OrderNotFoundException(String message) {
        super(message);
        this.item = new ItemErrorInfo(message);
    }

    public ItemErrorInfo getItem() {
        return item;
    }
}
