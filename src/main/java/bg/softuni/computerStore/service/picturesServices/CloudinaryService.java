package bg.softuni.computerStore.service.picturesServices;

import bg.softuni.computerStore.exception.MyFileDestroyFromCloudinaryException;
import bg.softuni.computerStore.exception.MyFileUploadException;
import bg.softuni.computerStore.model.entity.picture.CloudinaryImage;
import com.cloudinary.Cloudinary;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
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

    public CloudinaryImage upload(MultipartFile multipartFile) {
        File tempFile = null;
        String originalFilename = multipartFile.getOriginalFilename();
        try {
            tempFile = File.createTempFile(TEMP_file, originalFilename);
            multipartFile.transferTo(tempFile);

            @SuppressWarnings("unchecked")
            Map<String, String> uploadResult = cloudinary
                    .uploader()
                    .upload(tempFile, Map.of());

            //The long string is a funny photo for Error
            String url = uploadResult.getOrDefault(URL, FUNNY_PHOTO_FOR_ERROR);
            String publicId = uploadResult.getOrDefault(PUBLIC_ID, "");

            return new CloudinaryImage()
                    .setPublicId(publicId)
                    .setUrl(url);

        } catch (Exception e) {
            throw new MyFileUploadException(String.format("Can not upload file '%s'", originalFilename), e);
        } finally {
            tempFile.delete();
        }
    }

    public boolean deleteFromCloudinary(String publicId) {
        try {
            this.cloudinary.uploader().destroy(publicId, Map.of());
        } catch (Exception e) {
            throw new MyFileDestroyFromCloudinaryException(String.format("Error deleting from cloudinary a file with publicId '%s'", publicId), e);
        }

        return true;
    }
}
