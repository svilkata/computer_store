package bg.softuni.computerStore.service;

import bg.softuni.computerStore.init.InitializableProductService;
import bg.softuni.computerStore.model.binding.product.AddComputerBindingDTO;
import bg.softuni.computerStore.model.entity.products.ComputerEntity;
import bg.softuni.computerStore.repository.products.AllItemsRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class ComputerService implements InitializableProductService {
    private final AllItemsRepository allItemsRepository;

    public ComputerService(AllItemsRepository allItemsRepository) {
        this.allItemsRepository = allItemsRepository;
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
                    "8 GB DDR4 SoDIMM", null, "256 GB SSD M.2 NVMe",
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

    public Long saveComputer(AddComputerBindingDTO addComputerBindingDTO) {
        ComputerEntity toAdd = new ComputerEntity(addComputerBindingDTO.getBrand(), addComputerBindingDTO.getModel(),
                addComputerBindingDTO.getBuyingPrice(), addComputerBindingDTO.getSellingPrice(),
                addComputerBindingDTO.getNewQuantityToAdd(), addComputerBindingDTO.getMoreInfo());

        toAdd
                .setProcessor(addComputerBindingDTO.getProcessor())
                .setVideoCard(addComputerBindingDTO.getVideoCard())
                .setRam(addComputerBindingDTO.getRam())
                .setDisk(addComputerBindingDTO.getDisk())
                .setSsd(addComputerBindingDTO.getSsd());

        ComputerEntity saved = this.allItemsRepository.save(toAdd);
        return saved.getItemId();
    }
}
