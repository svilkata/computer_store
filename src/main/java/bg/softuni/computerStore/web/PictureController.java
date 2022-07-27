package bg.softuni.computerStore.web;

import bg.softuni.computerStore.model.binding.cloudinary.PictureBindingModel;
import bg.softuni.computerStore.model.entity.cloudinary.PictureEntity;
import bg.softuni.computerStore.service.picturesServices.CloudinaryImage;
import bg.softuni.computerStore.service.picturesServices.CloudinaryAndPictureService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@RequestMapping("/pages/purchases")
public class PictureController {
    private final CloudinaryAndPictureService cloudinaryAndPictureService;

    public PictureController(CloudinaryAndPictureService cloudinaryAndPictureService) {
        this.cloudinaryAndPictureService = cloudinaryAndPictureService;
    }

    @PostMapping("/computers/{id}/addpicture")
    public String addComputerPicture(PictureBindingModel bindingModel, @PathVariable("id") Long itemId) throws IOException {
        var picture = createPictureEntity(bindingModel.getPicture(), itemId);

        this.cloudinaryAndPictureService.savePhoto(picture);

        return "redirect:/items/all/computer/details/" + itemId;
    }

    @PostMapping("/monitors/{id}/addpicture")
    public String addMonitorPicture(PictureBindingModel bindingModel, @PathVariable("id") Long itemId) throws IOException {
        var picture = createPictureEntity(bindingModel.getPicture(), itemId);

        this.cloudinaryAndPictureService.savePhoto(picture);

        return "redirect:/items/all/monitor/details/" + itemId;
    }

    private PictureEntity createPictureEntity(MultipartFile multipartFile, Long itemId) throws IOException {
        final CloudinaryImage uploaded = this.cloudinaryAndPictureService.upload(multipartFile);

        return new PictureEntity()
                .setPublicId(uploaded.getPublicId())
                .setUrl(uploaded.getUrl())
                .setItemId(itemId);
    }


}
