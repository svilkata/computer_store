package bg.softuni.computerStore.service;

import bg.softuni.computerStore.config.mapper.StructMapper;
import bg.softuni.computerStore.init.InitializableProductService;
import bg.softuni.computerStore.model.binding.product.AddUpdateComputerBindingDTO;
import bg.softuni.computerStore.model.entity.products.ComputerEntity;
import bg.softuni.computerStore.model.entity.products.ItemEntity;
import bg.softuni.computerStore.model.view.product.ComputerViewGeneralModel;
import bg.softuni.computerStore.repository.products.AllItemsRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class ComputerService implements InitializableProductService {
    private final AllItemsRepository allItemsRepository;
    private final StructMapper structMapper;

    public ComputerService(AllItemsRepository allItemsRepository, StructMapper structMapper) {
        this.allItemsRepository = allItemsRepository;
        this.structMapper = structMapper;
    }

    @Override
    public void init() {
        if (allItemsRepository.findCounItemsByType("computer") < 1) {
            initOneComputer("Dell", "Dell Vostro 3681 SFF", 1000, 1150, 5,
                    "Intel Core i3-10100 (3.6/4.3GHz, 6M)", "Intel UHD Graphics 630",
                    "8 GB DDR4 2666 MHz", "1TB 7200rpm", "256 GB SSD M.2 NVMe",
                    "36 месеца международна гаранция Next Business Day");

            initOneComputer("Gigabyte", "Gigabyte Brix BRI5-10210E", 1550.2, 1650.75, 10,
                    "Intel Core i5-10210U (4.2 GHz, 6M)", "Intel UHD Graphics 620",
                    "8 GB DDR4 SoDIMM", "256 GB SSD M.2 NVMe", "",
                    "24 месеца гаранция");
        }
    }

    private void initOneComputer(String brand, String model, double buyAt, double sellAt, int newQuantity,
                                 String processor, String videoCard, String ram, String disk, String ssd,
                                 String extraInfo) {
        //With constructor
        ComputerEntity toAdd = new ComputerEntity(brand, model, BigDecimal.valueOf(buyAt), BigDecimal.valueOf(sellAt),
                newQuantity, extraInfo);
        toAdd
                .setProcessor(processor)
                .setVideoCard(videoCard)
                .setRam(ram)
                .setDisk(disk)
                .setSsd(ssd);

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
            allComputersView.add(this.structMapper.computerEntityToComputerSalesViewGeneralModel((ComputerEntity) item));
        }


        return allComputersView;
    }

    public void deleteComputerAndQuantity(Long id) {
        this.allItemsRepository.deleteById(id);
    }

    public ComputerViewGeneralModel findOneComputerById(Long id) {
        ItemEntity oneComputerById = this.allItemsRepository.findById(id).orElseThrow();

        return this.structMapper.computerEntityToComputerSalesViewGeneralModel((ComputerEntity) oneComputerById);
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
