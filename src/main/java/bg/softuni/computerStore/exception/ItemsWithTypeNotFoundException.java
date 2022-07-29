package bg.softuni.computerStore.exception;

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
