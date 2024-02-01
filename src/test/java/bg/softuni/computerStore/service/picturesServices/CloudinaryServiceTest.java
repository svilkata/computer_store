package bg.softuni.computerStore.service.picturesServices;

import bg.softuni.computerStore.exception.MyFileDestroyFromCloudinaryException;
import bg.softuni.computerStore.exception.MyFileUploadException;
import bg.softuni.computerStore.model.entity.picture.CloudinaryImage;
import com.cloudinary.Cloudinary;
import com.cloudinary.Uploader;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CloudinaryServiceTest {
    @Mock
    private Cloudinary cloudinary;

    @Test
    public void uploadMustReturnCorrectCloudinaryImage() throws IOException {
        // Arrange
        Uploader mockUploader = Mockito.mock(Uploader.class);
        when(cloudinary.uploader()).thenReturn(mockUploader);
        when(mockUploader.upload(any(File.class), any(Map.class)))
                .thenReturn(Map.of("url", "success_url", "public_id", "success_id"));
        CloudinaryService service = new CloudinaryService(cloudinary);

        // Act
        CloudinaryImage result = service.upload(new MockMultipartFile("FileName", new byte[0]));

        // Assert
        assertEquals("success_url", result.getUrl());
        assertEquals("success_id", result.getPublicId());
    }

    @Test
    public void uploadMustThrowException() throws IOException {
        // arrange
        Uploader mockUploader = Mockito.mock(Uploader.class);
        when(cloudinary.uploader()).thenReturn(mockUploader);
        when(mockUploader.upload(any(File.class), any(Map.class))).thenThrow(new IOException());
        CloudinaryService service = new CloudinaryService(cloudinary);

        // act & assert
        assertThrows(MyFileUploadException.class,
                () -> service.upload(new MockMultipartFile("FileName", new byte[0])),
                "Can not upload file 'FileName'");
    }

    @Test
    public void deleteFromCloudinaryTestMustDeleteAnImageFromCloudinary() throws IOException {
        // Arrange
        Uploader mockUploader = Mockito.mock(Uploader.class);
        when(cloudinary.uploader()).thenReturn(mockUploader);
        when(mockUploader.destroy(anyString(), any(Map.class)))
                .thenReturn(Map.of("url", "success_url", "public_id", "success_id"));
        CloudinaryService service = new CloudinaryService(cloudinary);

        // Act
        boolean isDeleted = service.deleteFromCloudinary("1");

        // Assert
        assertTrue(isDeleted);
    }

    @Test
    public void deleteFromCloudinaryTestMustThrowExceptionWhenDeleteAnImageFromCloudinary() throws IOException {
        // Arrange
        Uploader mockUploader = Mockito.mock(Uploader.class);
        when(cloudinary.uploader()).thenReturn(mockUploader);
        when(mockUploader.destroy(anyString(), any(Map.class)))
                .thenThrow(new IOException());
        CloudinaryService service = new CloudinaryService(cloudinary);

        // Act & Assert
//        boolean isDeleted = service.deleteFromCloudinary("1");
        assertThrows(MyFileDestroyFromCloudinaryException.class,
                () -> service.deleteFromCloudinary("1"),
                "Error deleting from cloudinary a file with publicId '1'");
    }
}