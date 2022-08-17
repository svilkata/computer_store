package bg.softuni.computerStore.exception;

public class MyFileDestroyFromCloudinaryException extends RuntimeException {
    private ItemErrorInfo item;

    public MyFileDestroyFromCloudinaryException(String message, Exception e) {
        super(message, e);
        this.item = new ItemErrorInfo(message);
    }

    public ItemErrorInfo getItem() {
        return item;
    }
}
