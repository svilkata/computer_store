package bg.softuni.computerStore.web;

import bg.softuni.computerStore.model.binding.cloudinary.PictureBindingModel;
import bg.softuni.computerStore.model.entity.cloudinary.PictureEntity;
import bg.softuni.computerStore.service.cloudinary.CloudinaryImage;
import bg.softuni.computerStore.service.cloudinary.CloudinaryService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;

@Controller
@RequestMapping("/pages/purchases")
public class PictureController {
    private final CloudinaryService cloudinaryService;


    public PictureController(CloudinaryService cloudinaryService) {
        this.cloudinaryService = cloudinaryService;
    }

    @PostMapping("/computers/{id}/addpicture")
    public String addPicture(PictureBindingModel bindingModel, @PathVariable("id") Long itemId) throws IOException {
        var picture = createPictureEntity(bindingModel.getPicture(), itemId);

        //TODO при презаписване на снимка
        this.cloudinaryService.savePhoto(picture);

        return "redirect:/items/all/computers/details/" + itemId;
    }

    private PictureEntity createPictureEntity(MultipartFile multipartFile, Long itemId) throws IOException {
        final CloudinaryImage uploaded = this.cloudinaryService.upload(multipartFile);

        return new PictureEntity()
                .setPublicId(uploaded.getPublicId())
                .setUrl(uploaded.getUrl())
                .setItemId(itemId);
    }


//    @Transactional
//    @DeleteMapping("/pictures/delete")
//    public String delete(@RequestParam("public_id") String publicId){
//        if (cloudinaryService.delete(publicId)) {
//            pictureRepository.deleteByPublicId(publicId);
//        }
//
//        return "redirect:/pictures/all";
//    }
}
