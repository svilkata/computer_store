package bg.softuni.computerStore.service;

import bg.softuni.computerStore.config.mapper.StructMapper;
import bg.softuni.computerStore.exception.ItemIdNotANumberException;
import bg.softuni.computerStore.exception.ItemNotFoundException;
import bg.softuni.computerStore.exception.ItemsWithTypeNotFoundException;
import bg.softuni.computerStore.initSeed.InitializableProductService;
import bg.softuni.computerStore.model.entity.products.ItemEntity;
import bg.softuni.computerStore.model.entity.products.LaptopEntity;
import bg.softuni.computerStore.model.view.product.LaptopViewGeneralModel;
import bg.softuni.computerStore.repository.products.AllItemsRepository;
import bg.softuni.computerStore.service.picturesServices.CloudinaryAndPictureService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class LaptopService implements InitializableProductService {
    private final AllItemsRepository allItemsRepository;
    private final StructMapper structMapper;
    private final CloudinaryAndPictureService cloudinaryAndPictureService;

    public LaptopService(AllItemsRepository allItemsRepository, StructMapper structMapper, CloudinaryAndPictureService cloudinaryAndPictureService) {
        this.allItemsRepository = allItemsRepository;
        this.structMapper = structMapper;
        this.cloudinaryAndPictureService = cloudinaryAndPictureService;
    }

    @Override
    public void init() {
        //do nothing for the moment
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
        } catch (Exception e){
            throw new ItemIdNotANumberException(String.format("%s is not a valid laptop item number!", itemId));
        }
        return itemLongId;
    }
}
