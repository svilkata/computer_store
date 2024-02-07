package bg.softuni.computerStore.service;

import bg.softuni.computerStore.config.mapper.StructMapper;
import bg.softuni.computerStore.exception.ItemNotFoundException;
import bg.softuni.computerStore.exception.ItemsWithTypeNotFoundException;
import bg.softuni.computerStore.exception.ObjectIdNotANumberException;
import bg.softuni.computerStore.initSeed.InitializableProductService;
import bg.softuni.computerStore.model.binding.product.AddUpdateComputerBindingDTO;
import bg.softuni.computerStore.model.binding.product.SearchProductItemDTO;
import bg.softuni.computerStore.model.entity.picture.PictureEntity;
import bg.softuni.computerStore.model.entity.products.ComputerEntity;
import bg.softuni.computerStore.model.entity.products.ItemEntity;
import bg.softuni.computerStore.model.view.product.ComputerViewGeneralModel;
import bg.softuni.computerStore.repository.products.AllItemsRepository;
import bg.softuni.computerStore.repository.products.ProductItemSpecification;
import bg.softuni.computerStore.service.picturesServices.PictureService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static bg.softuni.computerStore.constants.Constants.*;

@Service
public class ComputerService implements InitializableProductService {
    private final AllItemsRepository allItemsRepository;
    private final StructMapper structMapper;
    private final PictureService pictureService;

    public ComputerService(AllItemsRepository allItemsRepository, StructMapper structMapper, PictureService pictureService) {
        this.allItemsRepository = allItemsRepository;
        this.structMapper = structMapper;
        this.pictureService = pictureService;
    }

    @Override
    public void init() {
        if (allItemsRepository.findCountItemsByType("computer") < 1) {
            initOneComputer("Dell", "Dell Vostro 3681 SFF", 1000, 1150, 5,
                    "Intel Core i3-10100 (3.6/4.3GHz, 6M)", "Intel UHD Graphics 630",
                    "8 GB DDR4 2666 MHz", "1TB 7200rpm", "256 GB SSD M.2 NVMe",
                    "36 месеца международна гаранция Next Business Day",
                    IMAGE_PUBLIC_ID_COMPUTER_1);

            initOneComputer("Gigabyte", "Gigabyte Brix BRI5-10210E", 1550.2, 1650.75, 10,
                    "Intel Core i5-10210U (4.2 GHz, 6M)", "Intel UHD Graphics 620",
                    "8 GB DDR4 SoDIMM", "256 GB SSD M.2 NVMe", "",
                    "24 месеца гаранция",
                    IMAGE_PUBLIC_ID_COMPUTER_2);

            initOneComputer("Ardes", "Ardes Game - AGR54500RX6500XT", 1020.2, 1110.48, 8,
                    "AMD Ryzen 5 4500 (3.6/4.1GHz, 8M)", "AMD RX 6500 XT 4GB",
                    "8 GB DDR4 3200 MHz", "500 GB SSD M.2 NVMe", "",
                    "36 месеца гаранция",
                    IMAGE_PUBLIC_ID_COMPUTER_3);

            initOneComputer("Lenovo", "Lenovo ThinkCentre Neo 50s SFF - 11SX002VBL", 730, 840, 3,
                    "Intel Core i3-12100 (3.30 - 4.30 GHz, 12 MB Cache)", "Intel UHD Graphics 730",
                    "8 GB DDR4 3200 MHz", "256 GB SSD M.2 NVMe", "",
                    "36 месеца гаранция",
                    IMAGE_PUBLIC_ID_COMPUTER_4);

            initOneComputer("HP", "HP Pavilion 24-k1024nu All-in-One - 5Z7T6EA", 1700, 1807, 8,
                    "Intel Core i5-11500T (1.5/3.9GHz, 12M)", "Intel UHD Graphics 750",
                    "8 GB DDR4 2933 MHz SoDIMM", "512 GB SSD M.2 NVMe", "",
                    "23.8 (60.45cm) 1920x1080 IPS матов дисплей; 24 месеца гаранция",
                    IMAGE_PUBLIC_ID_COMPUTER_5);

            initOneComputer("ASUS", "ASUS PN51 Mini - 90MR00K1-M00800_16GBSODIMM_512NVMESSD_W10P", 1830, 1950, 7,
                    "AMD Ryzen 7 5700U (1.8/4.3GHz, 8M)", "AMD Radeon Vega 8 Graphics",
                    "16 GB DDR4 SoDIMM", "512 GB SSD M.2 NVMe", "",
                    "Поддържа M.2 SSD, Поддържа 2.5\" SSD/HDD.  24 месеца гаранция",
                    IMAGE_PUBLIC_ID_COMPUTER_6);

            // Zero
            initOneComputer("Lenovo", "Lenovo IdeaCentre 5 Tower - 90RW005VRI", 1510, 1611, 1,
                    "AMD Ryzen 5 5600G (3.9/4.4GHz, 16M)", "NVIDIA GTX 1650 Super 4GB",
                    "16 GB DDR4 3200 MHz", "512 GB SSD M.2 NVMe", "",
                    "24 месеца гаранция",
                    IMAGE_PUBLIC_ID_COMPUTER_7);
        }
    }

    private void initOneComputer(String brand, String model, double buyAt, double sellAt, int newQuantity,
                                 String processor, String videoCard, String ram, String disk, String ssd,
                                 String extraInfo, String photoPublicId) {
        //With constructor
        ComputerEntity toAdd = new ComputerEntity(brand, model, BigDecimal.valueOf(buyAt), BigDecimal.valueOf(sellAt),
                newQuantity, extraInfo);
        toAdd
                .setProcessor(processor)
                .setVideoCard(videoCard)
                .setRam(ram)
                .setDisk(disk)
                .setSsd(ssd);

        PictureEntity picture = this.pictureService.getPictureByPublicId(photoPublicId);
        toAdd.setPhoto(picture);

        this.allItemsRepository.save(toAdd.setCreationDateTime(LocalDateTime.now()));
    }

    public ComputerViewGeneralModel findOneComputerById(String itemId) {
        final Long id = isItemIdANumber(itemId);
        ItemEntity oneComputerById = this.allItemsRepository.findItemEntityByTypeAndItemId("computer", id)
                .orElseThrow(() -> new ItemNotFoundException(String.format("No computer item with id %d to be viewed!", id), id));

        return this.structMapper.computerEntityToComputerSalesViewGeneralModel((ComputerEntity) oneComputerById);
    }

    //we do not use this method now, but it is included in the tests
    public List<ComputerViewGeneralModel> findAllComputers() {
        List<ItemEntity> allComputers = this.allItemsRepository.findAllItemsByType("computer");
        if (allComputers.isEmpty()) {
            throw new ItemsWithTypeNotFoundException("No computers available in the database");
        }
        List<ComputerViewGeneralModel> allComputersView = new ArrayList<>();

        for (ItemEntity item : allComputers) {
            allComputersView.add(this.structMapper
                    .computerEntityToComputerSalesViewGeneralModel((ComputerEntity) item));
        }

        return allComputersView;
    }

    //Simpler option for pagination only
    public Page<ComputerViewGeneralModel> getAllComputersPageable(Pageable pageable) {

        return this.allItemsRepository
                .findAllByType("computer", pageable)
                .map(comp -> this.structMapper
                        .computerEntityToComputerSalesViewGeneralModel((ComputerEntity) comp));
    }


    //Complicated use
    public Page<ComputerViewGeneralModel> getAllComputersPageableAndSearched(Pageable pageable, SearchProductItemDTO searchProductItemDTO, String type) {

        return this.allItemsRepository
                .findAll(new ProductItemSpecification(searchProductItemDTO, type), pageable)
                .map(comp -> this.structMapper.computerEntityToComputerSalesViewGeneralModel((ComputerEntity) comp));
    }

    public Long saveNewComputer(AddUpdateComputerBindingDTO addUpdateComputerBindingDTO) {
        //From ItemEntity
        ComputerEntity toAdd = new ComputerEntity(addUpdateComputerBindingDTO.getBrand(), addUpdateComputerBindingDTO.getModel(),
                new BigDecimal(addUpdateComputerBindingDTO.getBuyingPrice()), new BigDecimal(addUpdateComputerBindingDTO.getSellingPrice()),
                addUpdateComputerBindingDTO.getNewQuantityToAdd(), addUpdateComputerBindingDTO.getMoreInfo());

        //From ComputerEntity
        toAdd
                .setProcessor(addUpdateComputerBindingDTO.getProcessor())
                .setVideoCard(addUpdateComputerBindingDTO.getVideoCard())
                .setRam(addUpdateComputerBindingDTO.getRam())
                .setDisk(addUpdateComputerBindingDTO.getDisk())
                .setSsd(addUpdateComputerBindingDTO.getSsd())
                .setCreationDateTime(LocalDateTime.now());

        ComputerEntity saved = this.allItemsRepository.save(toAdd);
        return saved.getItemId();
    }

    @Transactional
    public void deleteComputerAndQuantity(String itemId) {
        final Long id = isItemIdANumber(itemId);

        //Deleting a photo image from PictureRepository when deleting the Item itself
        ItemEntity itemEntityToDelete = this.allItemsRepository.findItemEntityByTypeAndItemId("computer", id)
                .orElseThrow(() -> new ItemNotFoundException(String.format("No computer with id %d to be deleted!", id), id));

        if (itemEntityToDelete.getPhoto() != null) {
            List<String> collect = Arrays.stream(itemEntityToDelete.getPhoto().getUrl().split("/")).toList();
            String publicId = collect.get(collect.size() - 1);
            publicId = publicId.substring(0, publicId.length() - 4);

            this.pictureService.deleteFromPictureRepository(publicId);
        }

        this.allItemsRepository.deleteById(id);
    }

    public AddUpdateComputerBindingDTO findComputerByIdUpdatingItem(String itemId) {
        final Long id = isItemIdANumber(itemId);

        ItemEntity oneComputerById = this.allItemsRepository.findById(id)
                .orElseThrow(() -> new ItemNotFoundException(String.format("No computer with id %d to be updated!", id), id));
        ComputerEntity ce = (ComputerEntity) oneComputerById;

        AddUpdateComputerBindingDTO addUpdateComputerBindingDTO = new AddUpdateComputerBindingDTO();
        //From ItemEntity
        addUpdateComputerBindingDTO
                .setItemId(id)
                .setBrand(oneComputerById.getBrand())
                .setModel(oneComputerById.getModel())
                .setCurrentQuantity(oneComputerById.getCurrentQuantity())
                .setBuyingPrice(oneComputerById.getBuyingPrice().toString())
                .setSellingPrice(oneComputerById.getSellingPrice().toString())
                .setMoreInfo(oneComputerById.getMoreInfo());

        //From AddUpdateComputerBindingDTO
        addUpdateComputerBindingDTO.setNewQuantityToAdd(0);

        //From ComputerEntity
        addUpdateComputerBindingDTO
                .setProcessor(ce.getProcessor())
                .setVideoCard(ce.getVideoCard())
                .setRam(ce.getRam())
                .setDisk(ce.getDisk())
                .setSsd(ce.getSsd());

        return addUpdateComputerBindingDTO;
    }

    public Long updateExistingComputer(AddUpdateComputerBindingDTO addUpdateComputerBindingDTO) {
        Long id = addUpdateComputerBindingDTO.getItemId();

        ItemEntity ce = this.allItemsRepository.findById(id)
                .orElseThrow(() -> new ItemNotFoundException(String.format("No computer with id %d to be updated!", id), id));
        ComputerEntity toUpdate = (ComputerEntity) ce;

        //From ItemEntity
        toUpdate
                .setBrand(addUpdateComputerBindingDTO.getBrand())
                .setModel(addUpdateComputerBindingDTO.getModel())
                .setBuyingPrice(new BigDecimal(addUpdateComputerBindingDTO.getBuyingPrice()))
                .setSellingPrice(new BigDecimal(addUpdateComputerBindingDTO.getSellingPrice()))
                .setCurrentQuantity(addUpdateComputerBindingDTO.getNewQuantityToAdd() + toUpdate.getCurrentQuantity())
                .setMoreInfo(addUpdateComputerBindingDTO.getMoreInfo());

        //From MonitorEntity
        toUpdate
                .setProcessor(addUpdateComputerBindingDTO.getProcessor())
                .setVideoCard(addUpdateComputerBindingDTO.getVideoCard())
                .setRam(addUpdateComputerBindingDTO.getRam())
                .setDisk(addUpdateComputerBindingDTO.getDisk())
                .setSsd(addUpdateComputerBindingDTO.getSsd());

        ComputerEntity saved = this.allItemsRepository.save(toUpdate);

        return saved.getItemId();
    }

    private Long isItemIdANumber(String itemId) {
        final long itemLongId;
        try {
            itemLongId = Long.parseLong(itemId);
        } catch (Exception e) {
            throw new ObjectIdNotANumberException(String.format("%s is not a valid computer item number!", itemId));
        }
        return itemLongId;
    }

}
