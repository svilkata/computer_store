package bg.softuni.computerStore.exception;

public class ItemErrorInfo {
    public final Long itemId;
    public final String errorMessage;

    public ItemErrorInfo(String errorMessage) {
        this.errorMessage = errorMessage;
        this.itemId = null;
    }

    public ItemErrorInfo(Long itemId, ItemNotFoundException ex) {
        this.itemId = itemId;
        this.errorMessage = ex.getLocalizedMessage();
    }

    public Long getItemId() {
        return itemId;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
