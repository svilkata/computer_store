package bg.softuni.computerStore.service;

import bg.softuni.computerStore.config.mapper.StructMapper;
import bg.softuni.computerStore.init.InitializableProductService;
import bg.softuni.computerStore.model.binding.product.AddUpdateComputerBindingDTO;
import bg.softuni.computerStore.model.entity.products.ComputerEntity;
import bg.softuni.computerStore.model.entity.products.ItemEntity;
import bg.softuni.computerStore.model.view.product.ComputerViewGeneralModel;
import bg.softuni.computerStore.repository.cloudinary.PictureRepository;
import bg.softuni.computerStore.repository.products.AllItemsRepository;
import bg.softuni.computerStore.service.cloudinary.CloudinaryService;
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
    private final CloudinaryService cloudinaryService;

    public ComputerService(AllItemsRepository allItemsRepository, StructMapper structMapper, PictureRepository pictureRepository, CloudinaryService cloudinaryService) {
        this.allItemsRepository = allItemsRepository;
        this.structMapper = structMapper;
        this.cloudinaryService = cloudinaryService;
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

    public Long saveNewComputer(AddUpdateComputerBindingDTO addUpdateComputerBindingDTO) {
        ComputerEntity toAdd = new ComputerEntity(addUpdateComputerBindingDTO.getBrand(), addUpdateComputerBindingDTO.getModel(),
                addUpdateComputerBindingDTO.getBuyingPrice(), addUpdateComputerBindingDTO.getSellingPrice(),
                addUpdateComputerBindingDTO.getNewQuantityToAdd(), addUpdateComputerBindingDTO.getMoreInfo());

        toAdd
                .setProcessor(addUpdateComputerBindingDTO.getProcessor())
                .setVideoCard(addUpdateComputerBindingDTO.getVideoCard())
                .setRam(addUpdateComputerBindingDTO.getRam())
                .setDisk(addUpdateComputerBindingDTO.getDisk())
                .setSsd(addUpdateComputerBindingDTO.getSsd());

        ComputerEntity saved = this.allItemsRepository.save(toAdd);
        return saved.getItemId();
    }

    public List<ComputerViewGeneralModel> findAllComputers() {
        List<ItemEntity> allComputers = this.allItemsRepository.findAllComputersByType("computer");
        List<ComputerViewGeneralModel> allComputersView = new ArrayList<>();

        for (ItemEntity item : allComputers) {
            allComputersView.add(this.structMapper
                    .computerEntityToComputerSalesViewGeneralModel((ComputerEntity) item));
        }


        return allComputersView;
    }

    @Transactional
    public void deleteComputerAndQuantity(Long id) {
        //Изтриване на снимка от PictureRepositoty при изтриване на самия Item
        ItemEntity itemEntityToDelete = this.allItemsRepository.findById(id).orElseThrow();
        List<String> collect = Arrays.stream(itemEntityToDelete.getPhotoUrl().split("/")).toList();
        String publicId = collect.get(collect.size()-1);
        publicId = publicId.substring(0, publicId.length()-4);
        if (this.cloudinaryService.deleteFromCloudinary(publicId)) {
            this.cloudinaryService.deleteFromPictureRepository(publicId);
        }

        this.allItemsRepository.deleteById(id);
    }

    public ComputerViewGeneralModel findOneComputerById(Long itemId) {
        ItemEntity oneComputerById = this.allItemsRepository.findById(itemId).orElseThrow();

        ComputerViewGeneralModel computerViewGeneralModel =
                this.structMapper.computerEntityToComputerSalesViewGeneralModel((ComputerEntity) oneComputerById);

        return computerViewGeneralModel;
    }

    public AddUpdateComputerBindingDTO findComputerByIdUpdatingItem(Long id) {
        ItemEntity oneComputerById = this.allItemsRepository.findById(id).orElseThrow();
        ComputerEntity ce = (ComputerEntity) oneComputerById;

        AddUpdateComputerBindingDTO addUpdateComputerBindingDTO = new AddUpdateComputerBindingDTO();
        addUpdateComputerBindingDTO
                .setItemId(id)
                .setBrand(oneComputerById.getBrand())
                .setModel(oneComputerById.getModel())
                .setCurrentQuantity(oneComputerById.getCurrentQuantity())
                .setNewQuantityToAdd(0)
                .setBuyingPrice(oneComputerById.getBuyingPrice())
                .setSellingPrice(oneComputerById.getSellingPrice())
                .setProcessor(ce.getProcessor())
                .setVideoCard(ce.getVideoCard())
                .setRam(ce.getRam())
                .setDisk(ce.getDisk())
                .setSsd(ce.getSsd())
                .setMoreInfo(ce.getMoreInfo());


        return addUpdateComputerBindingDTO;
    }

    public Long updateExistingComputer(AddUpdateComputerBindingDTO addUpdateComputerBindingDTO) {
        ItemEntity byId = this.allItemsRepository.findById(addUpdateComputerBindingDTO.getItemId()).orElseThrow();

        ComputerEntity toUpdate = new ComputerEntity(addUpdateComputerBindingDTO.getBrand(), addUpdateComputerBindingDTO.getModel(),
                addUpdateComputerBindingDTO.getBuyingPrice(), addUpdateComputerBindingDTO.getSellingPrice(),
                addUpdateComputerBindingDTO.getNewQuantityToAdd() + byId.getCurrentQuantity(),
                addUpdateComputerBindingDTO.getMoreInfo());

        toUpdate.setItemId(addUpdateComputerBindingDTO.getItemId());

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
