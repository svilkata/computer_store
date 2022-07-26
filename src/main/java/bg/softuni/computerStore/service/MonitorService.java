package bg.softuni.computerStore.service;

import bg.softuni.computerStore.config.mapper.StructMapper;
import bg.softuni.computerStore.init.InitializableProductService;
import bg.softuni.computerStore.model.binding.product.AddUpdateComputerBindingDTO;
import bg.softuni.computerStore.model.binding.product.AddUpdateMonitorBindingDTO;
import bg.softuni.computerStore.model.entity.products.ComputerEntity;
import bg.softuni.computerStore.model.entity.products.ItemEntity;
import bg.softuni.computerStore.model.entity.products.MonitorEntity;
import bg.softuni.computerStore.model.view.product.MonitorViewGeneralModel;
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

    public Long saveNewMonitor(AddUpdateMonitorBindingDTO addUpdateMonitorBindingDTO) {
        //From ItemEntity
        MonitorEntity toAdd = new MonitorEntity(addUpdateMonitorBindingDTO.getBrand(), addUpdateMonitorBindingDTO.getModel(),
                addUpdateMonitorBindingDTO.getBuyingPrice(), addUpdateMonitorBindingDTO.getSellingPrice(),
                addUpdateMonitorBindingDTO.getNewQuantityToAdd(), addUpdateMonitorBindingDTO.getMoreInfo());

        //From MonitorEntity
        toAdd
                .setSize(addUpdateMonitorBindingDTO.getSize())
                .setResolution(addUpdateMonitorBindingDTO.getResolution())
                .setMatrixType(addUpdateMonitorBindingDTO.getMatrixType())
                .setViewAngle(addUpdateMonitorBindingDTO.getViewAngle())
                .setRefreshRate(addUpdateMonitorBindingDTO.getRefreshRate())
                .setBrightness(addUpdateMonitorBindingDTO.getBrightness());

        MonitorEntity saved = this.allItemsRepository.save(toAdd);
        return saved.getItemId();
    }

    @Transactional
    public void deleteMonitorAndQuantity(Long id) {
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

    public AddUpdateMonitorBindingDTO findMonitorByIdUpdatingItem(Long id) {
        ItemEntity oneMonitorById = this.allItemsRepository.findById(id).orElseThrow();
        MonitorEntity me = (MonitorEntity) oneMonitorById;

        AddUpdateMonitorBindingDTO addUpdateMonitorBindingDTO = new AddUpdateMonitorBindingDTO();
        //From ItemEntity
        addUpdateMonitorBindingDTO
                .setItemId(id)
                .setBrand(oneMonitorById.getBrand())
                .setModel(oneMonitorById.getModel())
                .setCurrentQuantity(oneMonitorById.getCurrentQuantity())
                .setBuyingPrice(oneMonitorById.getBuyingPrice())
                .setSellingPrice(oneMonitorById.getSellingPrice())
                .setMoreInfo(oneMonitorById.getMoreInfo());

        //From AddUpdateMonitorBindingDTO
        addUpdateMonitorBindingDTO.setNewQuantityToAdd(0);

        //From MonitorEntity
        addUpdateMonitorBindingDTO
                .setSize(me.getSize())
                .setResolution(me.getResolution())
                .setMatrixType(me.getMatrixType())
                .setViewAngle(me.getViewAngle())
                .setRefreshRate(me.getRefreshRate())
                .setBrightness(me.getBrightness());

        return addUpdateMonitorBindingDTO;
    }

    public Long updateExistingMonitor(AddUpdateMonitorBindingDTO addUpdateMonitorBindingDTO) {
        ItemEntity me = this.allItemsRepository.findById(addUpdateMonitorBindingDTO.getItemId()).orElseThrow();
        MonitorEntity toUpdate = (MonitorEntity) me;

        //From ItemEntity
        toUpdate
                .setBrand(addUpdateMonitorBindingDTO.getBrand())
                .setModel(addUpdateMonitorBindingDTO.getModel())
                .setBuyingPrice(addUpdateMonitorBindingDTO.getBuyingPrice())
                .setSellingPrice(addUpdateMonitorBindingDTO.getSellingPrice())
                .setCurrentQuantity(addUpdateMonitorBindingDTO.getNewQuantityToAdd() + toUpdate.getCurrentQuantity())
                .setMoreInfo(addUpdateMonitorBindingDTO.getMoreInfo());

        //From MonitorEntity
        toUpdate
                .setSize(addUpdateMonitorBindingDTO.getSize())
                .setResolution(addUpdateMonitorBindingDTO.getResolution())
                .setMatrixType(addUpdateMonitorBindingDTO.getMatrixType())
                .setViewAngle(addUpdateMonitorBindingDTO.getViewAngle())
                .setRefreshRate(addUpdateMonitorBindingDTO.getRefreshRate())
                .setBrightness(addUpdateMonitorBindingDTO.getBrightness());

        MonitorEntity saved = this.allItemsRepository.save(toUpdate);

        return saved.getItemId();
    }
}
