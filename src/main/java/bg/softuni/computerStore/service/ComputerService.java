package bg.softuni.computerStore.service;

import bg.softuni.computerStore.init.InitializableProductService;
import bg.softuni.computerStore.model.entity.products.ComputerEntity;
import bg.softuni.computerStore.model.entity.products.ItemEntity;
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
            initOneComputer("Dell Vostro 3681 SFF", 1000, 1150, 5,
                    "Intel Core i3-10100 (3.6/4.3GHz, 6M)", "Intel UHD Graphics 630",
                    "8 GB DDR4 2666 MHz", "1TB 7200rpm", "256 GB SSD M.2 NVMe",
                    "36 месеца международна гаранция Next Business Day");

            initOneComputer("Gigabyte Brix BRI5-10210E", 1550.2, 1650.75, 10,
                    "Intel Core i5-10210U (4.2 GHz, 6M)", "Intel UHD Graphics 620",
                    "8 GB DDR4 SoDIMM", null, "256 GB SSD M.2 NVMe",
                    "24 месеца гаранция");
        }
    }

    private void initOneComputer(String name, double buyAt, double sellAt, int newQuantity,
                                 String processor, String videoCard, String ram, String disk, String ssd,
                                 String extraInfo) {
        ComputerEntity toAdd =
                new ComputerEntity(name, BigDecimal.valueOf(buyAt), BigDecimal.valueOf(sellAt), newQuantity);
        toAdd
                .setProcessor(processor)
                .setVideoCard(videoCard)
                .setRam(ram)
                .setDisk(disk)
                .setSsd(ssd)
                .setMoreInfo(extraInfo);

        this.allItemsRepository.save(toAdd);
    }
}
