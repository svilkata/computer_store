package bg.softuni.computerStore.service.cloudinary;

import bg.softuni.computerStore.init.InitializablePictureService;
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

import static bg.softuni.computerStore.constants.Constants.*;

@Service
public class CloudinaryService implements InitializablePictureService {
    private final Cloudinary cloudinary;
    private final PictureRepository pictureRepository;
    private final AllItemsRepository allItemsRepository;
    private static final String TEMP_file = "temp-file";
    private static final String URL = "url";
    private static final String PUBLIC_ID = "public_id";

    @Autowired
    public CloudinaryService(Cloudinary cloudinary, PictureRepository pictureRepository, AllItemsRepository allItemsRepository) {
        this.cloudinary = cloudinary;
        this.pictureRepository = pictureRepository;
        this.allItemsRepository = allItemsRepository;
    }

    @Override
    public void init() {
        if (pictureRepository.count() == 0) {
            initOnePicture(1L, IMAGE_PUBLIC_ID_COMPUTER_1, IMAGE_URL_COMPUTER_1);
            initOnePicture(2L, IMAGE_PUBLIC_ID_COMPUTER_2, IMAGE_URL_COMPUTER_2);
        }

    }

    private void initOnePicture(Long itemId, String publicId, String imageUrl) {
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
            String url = uploadResult.getOrDefault(URL, "https://thumbs.dreamstime.com/b/illustration-internet-connection-problem-concept-error-page-not-found-isolated-white-background-funny-blue-dinosaur-230224212.jpg");
            String publicId = uploadResult.getOrDefault(PUBLIC_ID, "");

            var result = new CloudinaryImage()
                    .setPublicId(publicId)
                    .setUrl(url);

            return result;
        } finally {
            tempFile.delete();
        }
    }

    public boolean delete(String publicId) {
        try {
            this.cloudinary.uploader().destroy(publicId, Map.of());
        } catch (IOException e) {
            return false;
        }

        return true;
    }

    public void savePhoto(PictureEntity picture) {
        //TODO delete old picture
        PictureEntity updatedPicture = new PictureEntity();
        Long id = picture.getId();
        updatedPicture.setId(picture.getId())
                .setItemId(picture.getItemId())
                .setPublicId(picture.getPublicId())
                .setPublicId(picture.getUrl());


        PictureEntity savedPhoto = this.pictureRepository.save(updatedPicture);
        ItemEntity byId = allItemsRepository.findById(savedPhoto.getItemId()).orElseThrow();
        byId.setPhotoUrl(savedPhoto.getUrl());
        this.allItemsRepository.save(byId);
    }


}
