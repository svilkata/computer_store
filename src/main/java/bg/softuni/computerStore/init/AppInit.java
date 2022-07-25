package bg.softuni.computerStore.init;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

@Component
public class AppInit {
    //Всички service класове, които сме имплементирали с InitializableService interface,
    // тук ни се зареждат автоматично - Open-Close principle
    private final InitializableUserService userServices;
    private final List<InitializableProductService> allProductServices;
    private final InitializableBasketService basketServices;
    private final InitializablePictureService pictureService;
    private final InitializableFinalOrderService finalOrderService;

    public AppInit(InitializableUserService userServices, List<InitializableProductService> allProductServices,
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
        this.userServices.init();  //1 init method
        this.pictureService.init();  //1 init method
        this.allProductServices.forEach(srvc -> srvc.init()); //many independent not in any order inits methods of eacg product
        this.basketServices.init();  //1 init method
        this.finalOrderService.init(); //1 init method
    }
}
