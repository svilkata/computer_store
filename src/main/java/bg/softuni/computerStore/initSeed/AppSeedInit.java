package bg.softuni.computerStore.initSeed;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

@Component
public class AppSeedInit {
    // All service classes that implement the InitializableService interface are loaded here - Open-Close principle
    private final InitializableUserService userServices;
    private final InitializablePictureService pictureService;
    private final List<InitializableProductService> allProductServices; //loads ComputerService, MonitorService and LaptopService
    private final InitializableBasketService basketServices;
    private final InitializableFinalOrderService finalOrderService;

    public AppSeedInit(InitializableUserService userServices, List<InitializableProductService> allProductServices,
                       InitializableBasketService basketServices, InitializablePictureService pictureService,
                       InitializableFinalOrderService finalOrderService) {
        this.userServices = userServices;
        this.allProductServices = allProductServices;
        this.basketServices = basketServices;
        this.pictureService = pictureService;
        this.finalOrderService = finalOrderService;
    }

    @PostConstruct
    public void beginInit() {
        this.userServices.init();  //1 initSeed method
        this.pictureService.init();  //1 initSeed method
        this.allProductServices.forEach(srvc -> srvc.init()); //Independent not in any order init methods of ComputerService, MonitorService and LaptopService
        this.basketServices.init();  //1 initSeed method
        this.finalOrderService.init(); //1 initSeed method
    }
}
