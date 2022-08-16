package bg.softuni.computerStore.service.picturesServices;

import bg.softuni.computerStore.model.entity.picture.CloudinaryImage;
import com.cloudinary.Cloudinary;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import static bg.softuni.computerStore.constants.Constants.FUNNY_PHOTO_FOR_ERROR;

@Service
public class CloudinaryService {
    private static final String TEMP_file = "temp-file";
    private static final String URL = "url";
    private static final String PUBLIC_ID = "public_id";

    private final Cloudinary cloudinary;

    public CloudinaryService(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
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

    public boolean deleteFromCloudinary(String publicId) {
        try {
            this.cloudinary.uploader().destroy(publicId, Map.of());
        } catch (IOException e) {
            return false;
        }

        return true;
    }
}
