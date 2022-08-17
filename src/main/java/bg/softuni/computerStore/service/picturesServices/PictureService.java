package bg.softuni.computerStore.service.picturesServices;

import bg.softuni.computerStore.initSeed.InitializablePictureService;
import bg.softuni.computerStore.model.entity.picture.CloudinaryImage;
import bg.softuni.computerStore.model.entity.picture.PictureEntity;
import bg.softuni.computerStore.model.entity.products.ItemEntity;
import bg.softuni.computerStore.repository.cloudinary.PictureRepository;
import bg.softuni.computerStore.repository.products.AllItemsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

import static bg.softuni.computerStore.constants.Constants.*;

@Service
public class PictureService implements InitializablePictureService {
    private final PictureRepository pictureRepository;
    private final AllItemsRepository allItemsRepository;
    private final CloudinaryService cloudinaryService;

    @Autowired
    public PictureService(PictureRepository pictureRepository, AllItemsRepository allItemsRepository, CloudinaryService cloudinaryService) {
        this.pictureRepository = pictureRepository;
        this.allItemsRepository = allItemsRepository;
        this.cloudinaryService = cloudinaryService;
    }

    @Override
    public void init() {
        if (pictureRepository.count() == 0) {
            initOnePicture(1L, IMAGE_PUBLIC_ID_COMPUTER_1, IMAGE_URL_COMPUTER_1);
            initOnePicture(2L, IMAGE_PUBLIC_ID_COMPUTER_2, IMAGE_URL_COMPUTER_2);
            initOnePicture(3L, IMAGE_PUBLIC_ID_COMPUTER_3, IMAGE_URL_COMPUTER_3);
            initOnePicture(4L, IMAGE_PUBLIC_ID_COMPUTER_4, IMAGE_URL_COMPUTER_4);
            initOnePicture(5L, IMAGE_PUBLIC_ID_COMPUTER_5, IMAGE_URL_COMPUTER_5);
            initOnePicture(6L, IMAGE_PUBLIC_ID_MONITOR_1, IMAGE_URL_MONITOR_1);
            initOnePicture(7L, IMAGE_PUBLIC_ID_MONITOR_2, IMAGE_URL_MONITOR_2);
            initOnePicture(8L, IMAGE_PUBLIC_ID_MONITOR_3, IMAGE_URL_MONITOR_3);
        }
    }

    private void initOnePicture(Long itemId, String publicId, String imageUrl) {
        PictureEntity picture = new PictureEntity();
        picture.setItemId(itemId).setPublicId(publicId)
                .setUrl(imageUrl);

        this.pictureRepository.save(picture);
    }

    public PictureEntity createPictureEntity(MultipartFile multipartFile, Long itemId) {
        final CloudinaryImage uploaded = this.cloudinaryService.upload(multipartFile);

        return new PictureEntity()
                .setPublicId(uploaded.getPublicId())
                .setUrl(uploaded.getUrl())
                .setItemId(itemId);
    }

    public void savePhoto(PictureEntity picture) {
        Optional<PictureEntity> pictureEntityOpt =
                this.pictureRepository.findPictureEntitiesByItemId(picture.getItemId());
        PictureEntity savedPhoto = null;

        if (pictureEntityOpt.isPresent()) { //updating existing photo
            PictureEntity updatedPictureEntity = pictureEntityOpt.get();
            String oldPublicId = updatedPictureEntity.getPublicId();

            //deleting from picturesServices the old photo with the old publicId
            this.cloudinaryService.deleteFromCloudinary(oldPublicId);

            updatedPictureEntity
                    .setUrl(picture.getUrl())
                    .setPublicId(picture.getPublicId());


            //Here we update the row in the table PictureRepository
            savedPhoto = this.pictureRepository.save(updatedPictureEntity);
        } else {
            //We save in a new row in the table PictureRepository
            savedPhoto = this.pictureRepository.save(picture);
        }

        //We update all the Items Repository here with the new url
        ItemEntity byId = allItemsRepository.findById(savedPhoto.getItemId()).orElseThrow();
        byId.setPhoto(savedPhoto); //we set the new photo
        this.allItemsRepository.save(byId);
    }

    public void deleteFromPictureRepository(String publicId) {
        if (this.cloudinaryService.deleteFromCloudinary(publicId)) {
            this.pictureRepository.deleteByPublicId(publicId);
        }
    }

    public PictureEntity getPictureByPublicId(String photoPublicId) {
        return this.pictureRepository.findPictureEntityByPublicId(photoPublicId).orElseThrow();
    }
}

