package bg.softuni.computerStore.service;

import bg.softuni.computerStore.init.InitializableProductService;
import bg.softuni.computerStore.model.entity.products.ItemEntity;
import bg.softuni.computerStore.model.entity.products.MonitorEntity;
import bg.softuni.computerStore.repository.products.MonitorRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class MonitorService implements InitializableProductService {
    private final MonitorRepository monitorRepository;

    public MonitorService(MonitorRepository monitorRepository) {
        this.monitorRepository = monitorRepository;
    }

    @Override
    public void init() {
        if (monitorRepository.count() == 0) {
            initOneMonitor("Monitor 23.8\" Dell PE 2422EE", 600.25, 650.55,
                    "23.8\" (60.45 cm)", "1920 x 1080", "IPS", "178/178", "60 Hz", "250 cd/m2",
                    "Интерфейси: USB Type-C с DisplayPort ф-я, DisplayPort цифров изход, Ethernet (RJ-45), 1 x HDMI 1.4, 1 x DisplayPort 1.2, 4 x USB 3.2 Downstream");

            initOneMonitor("Monitor 23.8\" Philips 243V7QDSB", 250.25, 330.22,
                    "23.8\" (60.5 cm)", "1920 x 1080", "IPS", "178/178", "75 Hz", "250 cd/m2",
                    "Интерфейси: VGA, 1 x HDMI, 1 x DVI-D порт, 1 x Audio Out");
        }
    }

    private void initOneMonitor(String name, double buyAt, double sellAt,
                                String sizeInInches, String resolution, String matrixType, String viewAngle,
                                String refreshRate, String brightness, String moreInfo) {

        ItemEntity toAdd = new MonitorEntity()
                .setName(name)
                .setBuyingPrice(BigDecimal.valueOf(buyAt))
                .setSellingPrice(BigDecimal.valueOf(sellAt));

        toAdd = ((MonitorEntity) toAdd)
                .setSize(sizeInInches)
                .setResolution(resolution)
                .setMatrixType(matrixType)
                .setViewAngle(viewAngle)
                .setRefreshRate(refreshRate)
                .setBrightness(brightness)
                .setMoreInfo(moreInfo);


        this.monitorRepository.save((MonitorEntity) toAdd);
    }
}
