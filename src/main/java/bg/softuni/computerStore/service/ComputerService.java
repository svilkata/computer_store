package bg.softuni.computerStore.service;

import bg.softuni.computerStore.config.mapper.StructMapper;
import bg.softuni.computerStore.initSeed.InitializableProductService;
import bg.softuni.computerStore.model.binding.product.AddUpdateComputerBindingDTO;
import bg.softuni.computerStore.model.entity.products.ComputerEntity;
import bg.softuni.computerStore.model.entity.products.ItemEntity;
import bg.softuni.computerStore.model.view.product.ComputerViewGeneralModel;
import bg.softuni.computerStore.repository.cloudinary.PictureRepository;
import bg.softuni.computerStore.repository.products.AllItemsRepository;
import bg.softuni.computerStore.service.picturesServices.CloudinaryAndPictureService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static bg.softuni.computerStore.constants.Constants.*;

@Service
public class ComputerService implements InitializableProductService {
    private final AllItemsRepository allItemsRepository;
    private final StructMapper structMapper;
    private final CloudinaryAndPictureService cloudinaryAndPictureService;

    public ComputerService(AllItemsRepository allItemsRepository, StructMapper structMapper, PictureRepository pictureRepository, CloudinaryAndPictureService cloudinaryAndPictureService) {
        this.allItemsRepository = allItemsRepository;
        this.structMapper = structMapper;
        this.cloudinaryAndPictureService = cloudinaryAndPictureService;
    }

    @Override
    public void init() {
        if (allItemsRepository.findCounItemsByType("computer") < 1) {
            initOneComputer("Dell", "Dell Vostro 3681 SFF", 1000, 1150, 5,
                    "Intel Core i3-10100 (3.6/4.3GHz, 6M)", "Intel UHD Graphics 630",
                    "8 GB DDR4 2666 MHz", "1TB 7200rpm", "256 GB SSD M.2 NVMe",
                    "36 месеца международна гаранция Next Business Day",
                    IMAGE_URL_COMPUTER_1);

            initOneComputer("Gigabyte", "Gigabyte Brix BRI5-10210E", 1550.2, 1650.75, 10,
                    "Intel Core i5-10210U (4.2 GHz, 6M)", "Intel UHD Graphics 620",
                    "8 GB DDR4 SoDIMM", "256 GB SSD M.2 NVMe", "",
                    "24 месеца гаранция",
                    IMAGE_URL_COMPUTER_2);

            initOneComputer("Ardes", "Ardes Game - AGR54500RX6500XT", 1020.2, 1110.48, 8,
                    "AMD Ryzen 5 4500 (3.6/4.1GHz, 8M)", "AMD RX 6500 XT 4GB",
                    "8 GB DDR4 3200 MHz", "500 GB SSD M.2 NVMe", "",
                    "36 месеца гаранция",
                    IMAGE_URL_COMPUTER_3);

            initOneComputer("Lenovo", "Lenovo ThinkCentre Neo 50s SFF - 11SX002VBL", 730, 840, 3,
                    "Intel Core i3-12100 (3.30 - 4.30 GHz, 12 MB Cache)", "Intel UHD Graphics 730",
                    "8 GB DDR4 3200 MHz", "256 GB SSD M.2 NVMe", "",
                    "36 месеца гаранция",
                    IMAGE_URL_COMPUTER_4);

            initOneComputer("HP", "HP Pavilion 24-k1024nu All-in-One - 5Z7T6EA", 1700, 1807, 4,
                    "Intel Core i5-11500T (1.5/3.9GHz, 12M)", "Intel UHD Graphics 750",
                    "8 GB DDR4 2933 MHz SoDIMM", "512 GB SSD M.2 NVMe", "",
                    "23.8 (60.45cm) 1920x1080 IPS матов дисплей; 24 месеца гаранция",
                    IMAGE_URL_COMPUTER_5);
        }
    }

    private void initOneComputer(String brand, String model, double buyAt, double sellAt, int newQuantity,
                                 String processor, String videoCard, String ram, String disk, String ssd,
                                 String extraInfo, String photoUrl) {
        //With constructor
        ComputerEntity toAdd = new ComputerEntity(brand, model, BigDecimal.valueOf(buyAt), BigDecimal.valueOf(sellAt),
                newQuantity, extraInfo);
        toAdd
                .setProcessor(processor)
                .setVideoCard(videoCard)
                .setRam(ram)
                .setDisk(disk)
                .setSsd(ssd)
                .setPhotoUrl(photoUrl);

        this.allItemsRepository.save(toAdd);
    }

    public ComputerViewGeneralModel findOneComputerById(Long itemId) {
        ItemEntity oneComputerById = this.allItemsRepository.findById(itemId).orElseThrow();

        ComputerViewGeneralModel computerViewGeneralModel =
                this.structMapper.computerEntityToComputerSalesViewGeneralModel((ComputerEntity) oneComputerById);

        return computerViewGeneralModel;
    }

    public List<ComputerViewGeneralModel> findAllComputers() {
        List<ItemEntity> allComputers = this.allItemsRepository.findAllItemsByType("computer");
        List<ComputerViewGeneralModel> allComputersView = new ArrayList<>();

        for (ItemEntity item : allComputers) {
            allComputersView.add(this.structMapper
                    .computerEntityToComputerSalesViewGeneralModel((ComputerEntity) item));
        }

        return allComputersView;
    }

    public Long saveNewComputer(AddUpdateComputerBindingDTO addUpdateComputerBindingDTO) {
        //From ItemEntity
        ComputerEntity toAdd = new ComputerEntity(addUpdateComputerBindingDTO.getBrand(), addUpdateComputerBindingDTO.getModel(),
                addUpdateComputerBindingDTO.getBuyingPrice(), addUpdateComputerBindingDTO.getSellingPrice(),
                addUpdateComputerBindingDTO.getNewQuantityToAdd(), addUpdateComputerBindingDTO.getMoreInfo());

        //From ComputerEntity
        toAdd
                .setProcessor(addUpdateComputerBindingDTO.getProcessor())
                .setVideoCard(addUpdateComputerBindingDTO.getVideoCard())
                .setRam(addUpdateComputerBindingDTO.getRam())
                .setDisk(addUpdateComputerBindingDTO.getDisk())
                .setSsd(addUpdateComputerBindingDTO.getSsd());

        ComputerEntity saved = this.allItemsRepository.save(toAdd);
        return saved.getItemId();
    }

    @Transactional
    public void deleteComputerAndQuantity(Long id) {
        //Изтриване на снимка от PictureRepositoty при изтриване на самия Item
        ItemEntity itemEntityToDelete = this.allItemsRepository.findById(id).orElseThrow();

        if (itemEntityToDelete.getPhotoUrl() != null) {
            List<String> collect = Arrays.stream(itemEntityToDelete.getPhotoUrl().split("/")).toList();
            String publicId = collect.get(collect.size() - 1);
            publicId = publicId.substring(0, publicId.length() - 4);
            if (this.cloudinaryAndPictureService.deleteFromCloudinary(publicId)) {
                this.cloudinaryAndPictureService.deleteFromPictureRepository(publicId);
            }
        }

        this.allItemsRepository.deleteById(id);
    }

    public AddUpdateComputerBindingDTO findComputerByIdUpdatingItem(Long id) {
        ItemEntity oneComputerById = this.allItemsRepository.findById(id).orElseThrow();
        ComputerEntity ce = (ComputerEntity) oneComputerById;

        AddUpdateComputerBindingDTO addUpdateComputerBindingDTO = new AddUpdateComputerBindingDTO();
        //From ItemEntity
        addUpdateComputerBindingDTO
                .setItemId(id)
                .setBrand(oneComputerById.getBrand())
                .setModel(oneComputerById.getModel())
                .setCurrentQuantity(oneComputerById.getCurrentQuantity())
                .setBuyingPrice(oneComputerById.getBuyingPrice())
                .setSellingPrice(oneComputerById.getSellingPrice())
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
        ItemEntity ce = this.allItemsRepository.findById(addUpdateComputerBindingDTO.getItemId()).orElseThrow();
        ComputerEntity toUpdate = (ComputerEntity) ce;

        //From ItemEntity
        toUpdate
                .setBrand(addUpdateComputerBindingDTO.getBrand())
                .setModel(addUpdateComputerBindingDTO.getModel())
                .setBuyingPrice(addUpdateComputerBindingDTO.getBuyingPrice())
                .setSellingPrice(addUpdateComputerBindingDTO.getSellingPrice())
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
}
