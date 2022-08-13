package bg.softuni.computerStore.service.picturesServices;

import bg.softuni.computerStore.initSeed.InitializablePictureService;
import bg.softuni.computerStore.model.entity.cloudinary.PictureEntity;
import bg.softuni.computerStore.model.entity.products.ItemEntity;
import bg.softuni.computerStore.repository.cloudinary.PictureRepository;
import bg.softuni.computerStore.repository.products.AllItemsRepository;
import com.cloudinary.Cloudinary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;

import static bg.softuni.computerStore.constants.Constants.*;

@Service
public class CloudinaryAndPictureService implements InitializablePictureService {
    private final Cloudinary cloudinary;
    private final PictureRepository pictureRepository;
    private final AllItemsRepository allItemsRepository;
    private static final String TEMP_file = "temp-file";
    private static final String URL = "url";
    private static final String PUBLIC_ID = "public_id";

    @Autowired
    public CloudinaryAndPictureService(Cloudinary cloudinary, PictureRepository pictureRepository, AllItemsRepository allItemsRepository) {
        this.cloudinary = cloudinary;
        this.pictureRepository = pictureRepository;
        this.allItemsRepository = allItemsRepository;
    }

    @Override
    public Long init() {
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

        return 8L;
    }

    private void initOnePicture(Long itemId, String publicId, String imageUrl) {
        int a = 5;
        PictureEntity picture = new PictureEntity();
        picture.setItemId(itemId).setPublicId(publicId)
                .setUrl(imageUrl);

        this.pictureRepository.save(picture);
    }


    public CloudinaryImage upload(MultipartFile multipartFile) throws IOException {
        File tempFile = File.createTempFile(TEMP_file, multipartFile.getOriginalFilename());
        multipartFile.transferTo(tempFile);

        try {
            @SuppressWarnings("unchecked")
            Map<String, String> uploadResult = cloudinary
                    .uploader()
                    .upload(tempFile, Map.of());
//                    .upload(tempFile, Map.of("use_filename"));

            //The long string is a funny photo for Error
            String url = uploadResult.getOrDefault(URL, FUNNY_PHOTO_FOR_ERROR);
            String publicId = uploadResult.getOrDefault(PUBLIC_ID, "");

            var result = new CloudinaryImage()
                    .setPublicId(publicId)
                    .setUrl(url);

            return result;
        } finally {
            tempFile.delete();
        }
    }


    public void savePhoto(PictureEntity picture) {
        Optional<PictureEntity> pictureEntityOpt =
                this.pictureRepository.findPictureEntitiesByItemId(picture.getItemId());
        PictureEntity savedPhoto = null;
        if (pictureEntityOpt.isPresent()) {
            PictureEntity updatedPictureEntity = pictureEntityOpt.get();
            String oldPublicId = updatedPictureEntity.getPublicId();

            //deleting from picturesServices the old photo with the old publicId
            deleteFromCloudinary(oldPublicId);

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

    public boolean deleteFromCloudinary(String publicId) {
        try {
            this.cloudinary.uploader().destroy(publicId, Map.of());
        } catch (IOException e) {
            return false;
        }

        return true;
    }

    public void deleteFromPictureRepository(String publicId) {
        this.pictureRepository.deleteByPublicId(publicId);
    }

    public PictureEntity getPictureByPublicId(String photoPublicId) {
        return this.pictureRepository.findPictureEntityByPublicId(photoPublicId).orElseThrow();
    }
}
