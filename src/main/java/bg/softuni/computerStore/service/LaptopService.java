package bg.softuni.computerStore.service;

import bg.softuni.computerStore.config.mapper.StructMapper;
import bg.softuni.computerStore.exception.ObjectIdNotANumberException;
import bg.softuni.computerStore.exception.ItemNotFoundException;
import bg.softuni.computerStore.exception.ItemsWithTypeNotFoundException;
import bg.softuni.computerStore.initSeed.InitializableProductService;
import bg.softuni.computerStore.model.entity.picture.PictureEntity;
import bg.softuni.computerStore.model.entity.products.ComputerEntity;
import bg.softuni.computerStore.model.entity.products.ItemEntity;
import bg.softuni.computerStore.model.entity.products.LaptopEntity;
import bg.softuni.computerStore.model.view.product.LaptopViewGeneralModel;
import bg.softuni.computerStore.repository.products.AllItemsRepository;
import bg.softuni.computerStore.service.picturesServices.PictureService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static bg.softuni.computerStore.constants.Constants.IMAGE_PUBLIC_ID_COMPUTER_1;
import static bg.softuni.computerStore.constants.Constants.IMAGE_PUBLIC_ID_LAPTOP_1;

@Service
public class LaptopService implements InitializableProductService {
    private final AllItemsRepository allItemsRepository;
    private final StructMapper structMapper;
    private final PictureService pictureService;

    public LaptopService(AllItemsRepository allItemsRepository, StructMapper structMapper, PictureService pictureService) {
        this.allItemsRepository = allItemsRepository;
        this.structMapper = structMapper;
        this.pictureService = pictureService;
    }

    @Override
    public void init() {
        if (allItemsRepository.findCounItemsByType("laptop") < 1) {
            initOneLaptop("Dell", "Dell Mobile Vostro 1234", 1300, 1400, 6,
                    "1920x1080",
                    "extra info bla bla bla", IMAGE_PUBLIC_ID_LAPTOP_1);
        }
    }

    private void initOneLaptop(String brand, String model, double buyAt, double sellAt, int newQuantity,
                               String resolution,
                               String moreInfo, String photoPublicId) {
        //With constructor
        LaptopEntity toAdd = new LaptopEntity(brand, model, BigDecimal.valueOf(buyAt), BigDecimal.valueOf(sellAt),
                newQuantity, moreInfo);
        toAdd
                .setResolution(resolution);


        PictureEntity picture = this.pictureService.getPictureByPublicId(photoPublicId);
        toAdd.setPhoto(picture);

        this.allItemsRepository.save(toAdd.setCreationDateTime(LocalDateTime.now()));
    }

    public LaptopViewGeneralModel findOneLaptopById(String itemId) {
        final Long id = isItemIdANumber(itemId);
        ItemEntity oneLaptopById = this.allItemsRepository.findItemEntityByTypeAndItemId("laptop", id)
                .orElseThrow(() -> new ItemNotFoundException(String.format("No laptop item with id %d to be viewed!", id), id));

        LaptopViewGeneralModel laptopViewGeneralModel =
                this.structMapper.laptopEntityToLaptopViewGeneralModel((LaptopEntity) oneLaptopById);

        return laptopViewGeneralModel;
    }

    public List<LaptopViewGeneralModel> findAllLaptops() {
        List<ItemEntity> allLaptops = this.allItemsRepository.findAllItemsByType("laptop");
        if (allLaptops.isEmpty()) {
            throw new ItemsWithTypeNotFoundException("No laptops available in the database");
        }
        List<LaptopViewGeneralModel> allMonitorsView = new ArrayList<>();

        for (ItemEntity item : allLaptops) {
            allMonitorsView.add(this.structMapper
                    .laptopEntityToLaptopViewGeneralModel((LaptopEntity) item));
        }

        return allMonitorsView;
    }

    private Long isItemIdANumber(String itemId) {
        final Long itemLongId;
        try {
            itemLongId = Long.parseLong(itemId);
        } catch (Exception e) {
            throw new ObjectIdNotANumberException(String.format("%s is not a valid laptop item number!", itemId));
        }
        return itemLongId;
    }
}
