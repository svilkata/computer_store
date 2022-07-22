package bg.softuni.computerStore.service;

import bg.softuni.computerStore.init.InitializableProductService;
import bg.softuni.computerStore.model.entity.products.MonitorEntity;
import bg.softuni.computerStore.repository.products.AllItemsRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class MonitorService implements InitializableProductService {
    private final AllItemsRepository allItemsRepository;

    public MonitorService(AllItemsRepository allItemsRepository) {
        this.allItemsRepository = allItemsRepository;
    }

    @Override
    public void init() {
        if (allItemsRepository.findCounItemsByType("monitor") < 1) {
            initOneMonitor("Dell", "Monitor 23.8\" Dell PE 2422EE", 600.25, 650.55, 8,
                    "23.8\" (60.45 cm)", "1920 x 1080", "IPS", "178/178",
                    "60 Hz", "250 cd/m2",
                    "Интерфейси: USB Type-C с DisplayPort ф-я, DisplayPort цифров изход, Ethernet (RJ-45), 1 x HDMI 1.4, 1 x DisplayPort 1.2, 4 x USB 3.2 Downstream");

            initOneMonitor("Philips", "Monitor 23.8\" Philips 243V7QDSB", 250.25, 330.22, 9,
                    "23.8\" (60.5 cm)", "1920 x 1080", "IPS", "178/178",
                    "75 Hz", "250 cd/m2", "Интерфейси: VGA, 1 x HDMI, 1 x DVI-D порт, 1 x Audio Out");
        }
    }

    private void initOneMonitor(String brand, String mnodel, double buyAt, double sellAt, int newQuantity,
                                String sizeInInches, String resolution, String matrixType, String viewAngle,
                                String refreshRate, String brightness,
                                String moreInfo) {

        MonitorEntity toAdd =
                new MonitorEntity(brand, mnodel, BigDecimal.valueOf(buyAt), BigDecimal.valueOf(sellAt), newQuantity, moreInfo);

        toAdd
                .setSize(sizeInInches)
                .setResolution(resolution)
                .setMatrixType(matrixType)
                .setViewAngle(viewAngle)
                .setRefreshRate(refreshRate)
                .setBrightness(brightness)
                .setMoreInfo(moreInfo);


        this.allItemsRepository.save(toAdd);
    }
}
