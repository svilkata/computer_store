package bg.softuni.computerStore.service.picturesServices;

import bg.softuni.computerStore.model.entity.picture.PictureEntity;
import bg.softuni.computerStore.model.entity.products.ComputerEntity;
import bg.softuni.computerStore.model.entity.products.ItemEntity;
import bg.softuni.computerStore.repository.cloudinary.PictureRepository;
import bg.softuni.computerStore.repository.products.AllItemsRepository;
import com.cloudinary.Cloudinary;
import com.cloudinary.Uploader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

//@ExtendWith(MockitoExtension.class)
@AutoConfigureTestDatabase
@SpringBootTest
//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PictureServiceTest {
    @Autowired
    private PictureService pictureService;

    private PictureEntity testPictureEntity1, testPictureEntity2;
    private ItemEntity testItemEntity;
    private String testPhotoPublicId = "abcd";

    //Mocked objects
    @Mock
    private CloudinaryService mockedCloudinaryService;
    @Mock
    private PictureRepository mockedPictureRepository;
    @Mock
    private AllItemsRepository mockedAllItemsRepository;
    @Mock
    private Cloudinary mockedCloudinary;
    @Mock
    private Uploader mockedUploader;

    @BeforeEach
    public void setup() throws IOException {
        //Arrange
        testPictureEntity1 = (new PictureEntity()
                .setId(1L)
                .setPublicId(testPhotoPublicId)
                .setUrl("bala")
                .setItemId(1L));

        testPictureEntity2 = (new PictureEntity()
                .setId(2L)
                .setPublicId("efgh")
                .setUrl("bala")
                .setItemId(2L));

        // Arrange
        mockedUploader = Mockito.mock(Uploader.class);
        when(mockedCloudinary.uploader()).thenReturn(mockedUploader);
        when(mockedUploader.upload(any(File.class), any(Map.class)))
                .thenReturn(Map.of("url", "success_url", "public_id", "success_id"));
        when(mockedUploader.destroy(anyString(), any(Map.class)))
                .thenReturn(Map.of("url", "success_url", "public_id", "success_id"));
        this.mockedCloudinaryService = new CloudinaryService(mockedCloudinary);

        this.pictureService = new PictureService(mockedPictureRepository, mockedAllItemsRepository, mockedCloudinaryService);
    }

    @Test
    void createPictureEntityTestSuccessfull() {
        //More to arrange
        MockMultipartFile mockedMultipartFile = new MockMultipartFile(
                "file",
                "hello.txt",
                MediaType.TEXT_PLAIN_VALUE,
                "Hello World".getBytes());

        //Act
        PictureEntity createdPictureEntity =
                this.pictureService.createPictureEntity(mockedMultipartFile, 8L);

        //Assert
        assertEquals(createdPictureEntity.getItemId(), 8L);
    }

    @Test
    void getPictureByPublicIdTestSuccessfull() {
        //Act
        PictureEntity expected = testPictureEntity1;

//        when(mockedPictureRepository.findPictureEntityByPublicId(testPhotoPublicId))
//                .thenReturn(Optional.ofNullable(testPictureEntity1));
        doReturn(Optional.of(testPictureEntity1)).when(mockedPictureRepository).findPictureEntityByPublicId(testPhotoPublicId);

        PictureEntity result = this.pictureService.getPictureByPublicId(testPhotoPublicId);

        //Assert
        assertEquals(expected.getPublicId(), result.getPublicId());
//        assertThrows(NoSuchElementException.class,
//                () -> this.pictureService.getPictureByPublicId(testPhotoPublicId));
    }

    @Test
    void deleteFromPictureRepositoryTestSuccessfull() {
        this.pictureService.deleteFromPictureRepository(testPhotoPublicId);
    }

    @Test
    void savePhotoTestSuccessfullWhenPictureIsPresent() {
//        when(mockedPictureRepository.findPictureEntitiesByItemId(1L))
//                .thenReturn(Optional.ofNullable(testPictureEntity1));
        doReturn(Optional.of(testPictureEntity1)).when(mockedPictureRepository).findPictureEntitiesByItemId(1L);

        when(mockedPictureRepository.save(testPictureEntity1))
                .thenReturn(testPictureEntity1);

        testItemEntity = new ComputerEntity();
        testItemEntity
                .setItemId(1L)
                .setBrand("AK 47")
                .setModel("Naj-dpbria")
                .setCurrentQuantity(3)
                .setBuyingPrice(BigDecimal.valueOf(12))
                .setSellingPrice(BigDecimal.valueOf(18))
                .setCreationDateTime(LocalDateTime.now());

//        when(mockedAllItemsRepository.findById(1L))
//                .thenReturn(Optional.ofNullable(testItemEntity));
        doReturn(Optional.of(testItemEntity)).when(mockedAllItemsRepository).findById(1L);

        this.pictureService.savePhoto(testPictureEntity1);
    }

    @Test
    void savePhotoTestSuccessfullWhenPictureIsAbsent() {
        doReturn(Optional.empty()).when(mockedPictureRepository).findPictureEntitiesByItemId(2L);

        when(mockedPictureRepository.save(testPictureEntity2))
                .thenReturn(testPictureEntity2);

        testItemEntity = new ComputerEntity();
        testItemEntity
                .setItemId(1L)
                .setBrand("AK 47")
                .setModel("Naj-dpbria")
                .setCurrentQuantity(3)
                .setBuyingPrice(BigDecimal.valueOf(12))
                .setSellingPrice(BigDecimal.valueOf(18))
                .setCreationDateTime(LocalDateTime.now());

        when(mockedAllItemsRepository.findById(2L))
                .thenReturn(Optional.ofNullable(testItemEntity));
//        doReturn(Optional.of(testItemEntity)).when(mockedAllItemsRepository).findById(1L);

        this.pictureService.savePhoto(testPictureEntity2);
    }
}