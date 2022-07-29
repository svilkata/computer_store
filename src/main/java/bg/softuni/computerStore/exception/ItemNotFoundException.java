package bg.softuni.computerStore.exception;

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
