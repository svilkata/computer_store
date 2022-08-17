package bg.softuni.computerStore.web;

import bg.softuni.computerStore.model.binding.cloudinary.PictureBindingModel;
import bg.softuni.computerStore.model.entity.picture.PictureEntity;
import bg.softuni.computerStore.model.entity.picture.CloudinaryImage;
import bg.softuni.computerStore.service.picturesServices.PictureService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@RequestMapping("/pages/purchases")
public class PictureController {
    private final PictureService pictureService;

    public PictureController(PictureService pictureService) {
        this.pictureService = pictureService;
    }

    @PostMapping("/computers/{itemId}/addpicture")
    public String addComputerPicture(PictureBindingModel bindingModel, @PathVariable("itemId") Long itemId) {
        PictureBindingModel aaa = bindingModel;
        var picture = this.pictureService.createPictureEntity(bindingModel.getPicture(), itemId);

        this.pictureService.savePhoto(picture);

        return "redirect:/items/all/computer/details/" + itemId;
    }

    @PostMapping("/monitors/{id}/addpicture")
    public String addMonitorPicture(PictureBindingModel bindingModel, @PathVariable("id") Long itemId) {
        var picture = this.pictureService.createPictureEntity(bindingModel.getPicture(), itemId);

        this.pictureService.savePhoto(picture);

        return "redirect:/items/all/monitor/details/" + itemId;
    }
}
