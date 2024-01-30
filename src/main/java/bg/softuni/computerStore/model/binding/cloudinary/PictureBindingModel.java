package bg.softuni.computerStore.model.binding.cloudinary;

import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;

public class PictureBindingModel {
    @NotNull
    private MultipartFile picture;

    public PictureBindingModel() {
    }

    public MultipartFile getPicture() {
        return picture;
    }

    public PictureBindingModel setPicture(MultipartFile picture) {
        this.picture = picture;
        return this;
    }
}
