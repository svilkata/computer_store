package bg.softuni.computerStore.exception;

public class ObjectIdNotANumberException extends RuntimeException  {
    private ItemErrorInfo item;

    public ObjectIdNotANumberException(String message) {
        super(message);
        this.item = new ItemErrorInfo(message);
    }

    public ItemErrorInfo getItem() {
        return item;
    }
}
