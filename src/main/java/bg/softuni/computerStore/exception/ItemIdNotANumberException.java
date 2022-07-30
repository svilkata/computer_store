package bg.softuni.computerStore.exception;

public class ItemIdNotANumberException extends RuntimeException  {
    private ItemErrorInfo item;

    public ItemIdNotANumberException(String message) {
        super(message);
        this.item = new ItemErrorInfo(message);
    }

    public ItemErrorInfo getItem() {
        return item;
    }
}
