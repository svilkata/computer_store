package bg.softuni.computerStore.service;

import bg.softuni.computerStore.config.mapper.StructMapper;
import bg.softuni.computerStore.init.InitializableProductService;
import bg.softuni.computerStore.model.entity.products.ItemEntity;
import bg.softuni.computerStore.model.entity.products.MonitorEntity;
import bg.softuni.computerStore.model.view.product.MonitorViewGeneralModel;
import bg.softuni.computerStore.repository.products.AllItemsRepository;
import bg.softuni.computerStore.service.picturesServices.CloudinaryAndPictureService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static bg.softuni.computerStore.constants.Constants.*;

@Service
public class MonitorService implements InitializableProductService {
    private final AllItemsRepository allItemsRepository;
    private final StructMapper structMapper;
    private final CloudinaryAndPictureService cloudinaryAndPictureService;

    public MonitorService(AllItemsRepository allItemsRepository, StructMapper structMapper, CloudinaryAndPictureService cloudinaryAndPictureService) {
        this.allItemsRepository = allItemsRepository;
        this.structMapper = structMapper;
        this.cloudinaryAndPictureService = cloudinaryAndPictureService;
    }

    @Override
    public void init() {
        if (allItemsRepository.findCounItemsByType("monitor") < 1) {
            initOneMonitor("LG", "LG 29WP500-B - 29 LG 29WP500-B", 430.87, 521.69, 7,
                    "29\" (73.66 cm)", "2560 x 1080", "IPS", "178/178",
                    "75 Hz", "250 cd/m2",
                    "G-Sync/FreeSync: AMD FreeSync; Интерфейси: Аудио изход за слушалки, 2 x HDMI",
                    IMAGE_URL_MONITOR_1);

            initOneMonitor("ASUS", "ASUS VA27EHE - 90LM0550-B01170", 301.01, 349.00, 4,
                    "27\" (68.58 cm)", "1920 x 1080", "IPS", "178/178",
                    "75 Hz", "250 cd/m2", "G-Sync/FreeSync: AMD FreeSync; Интерфейси: VGA, 1 x HDMI",
                    IMAGE_URL_MONITOR_2);

            initOneMonitor("Acer", "Acer SB220QBI - SB220QBI", 238.01, 277.43, 4,
                    "21.5\" (54.61 cm)", "1920 x 1080", "IPS", "178/178",
                    "75 Hz", "250 cd/m2", "Интерфейси: VGA, 1 x HDMI 1.4",
                    IMAGE_URL_MONITOR_3);
        }
    }

    private void initOneMonitor(String brand, String model, double buyAt, double sellAt, int newQuantity,
                                String sizeInInches, String resolution, String matrixType, String viewAngle,
                                String refreshRate, String brightness,
                                String moreInfo, String photoUrl) {

        MonitorEntity toAdd =
                new MonitorEntity(brand, model, BigDecimal.valueOf(buyAt), BigDecimal.valueOf(sellAt), newQuantity, moreInfo);

        toAdd
                .setSize(sizeInInches)
                .setResolution(resolution)
                .setMatrixType(matrixType)
                .setViewAngle(viewAngle)
                .setRefreshRate(refreshRate)
                .setBrightness(brightness)
                .setMoreInfo(moreInfo)
                .setPhotoUrl(photoUrl);

        this.allItemsRepository.save(toAdd);
    }

    public MonitorViewGeneralModel findOneMonitorById(Long itemId) {
        ItemEntity oneMonitorById = this.allItemsRepository.findById(itemId).orElseThrow();

        MonitorViewGeneralModel monitorViewGeneralModel =
                this.structMapper.monitorEntityToMonitorViewGeneralModel((MonitorEntity) oneMonitorById);

        return monitorViewGeneralModel;
    }

    public List<MonitorViewGeneralModel> findAllMonitors() {
        List<ItemEntity> allMonitors = this.allItemsRepository.findAllItemsByType("monitor");
        List<MonitorViewGeneralModel> allMonitorsView = new ArrayList<>();

        for (ItemEntity item : allMonitors) {
            allMonitorsView.add(this.structMapper
                    .monitorEntityToMonitorViewGeneralModel((MonitorEntity) item));
        }

        return allMonitorsView;
    }

}
