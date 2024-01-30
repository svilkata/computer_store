package bg.softuni.computerStore.exception;

//import org.springframework.http.HttpStatus;
//import org.springframework.web.bind.annotation.ResponseStatus;

//@ResponseStatus(value= HttpStatus.CONFLICT, reason="Error uploading a file")
public class MyFileUploadException extends RuntimeException {
    private ItemErrorInfo item;

    public MyFileUploadException(String message, Exception e) {
        super(message, e);
        this.item = new ItemErrorInfo(message);
    }

    public ItemErrorInfo getItem() {
        return item;
    }
}
