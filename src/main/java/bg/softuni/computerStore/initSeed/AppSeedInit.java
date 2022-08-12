package bg.softuni.computerStore.initSeed;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

@Component
public class AppSeedInit {
    //Всички service класове, които сме имплементирали с InitializableService interface,
    // тук ни се зареждат автоматично - Open-Close principle
    private final InitializableUserService userServices;
    private final List<InitializableProductService> allProductServices;
    private final InitializableBasketService basketServices;
    private final InitializablePictureService pictureService;
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

//    @PostConstruct
//    public void beginInit() {
//        this.userServices.init();  //1 initSeed method
//        this.pictureService.init();  //1 initSeed method
//        this.allProductServices.forEach(srvc -> srvc.init()); //many independent not in any order inits methods of eacg product
//        this.basketServices.init();  //1 initSeed method
//        this.finalOrderService.init(); //1 initSeed method
//    }
}
