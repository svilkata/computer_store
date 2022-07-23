package bg.softuni.computerStore.init;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

@Component
public class AppInit {
    //Всички service класове, които сме имплементирали с InitializableService interface,
    // тук ни се зареждат автоматично - Open-Close principle
    private final InitializableUserService allUserServices;
    private final List<InitializableProductService> allProductServices;
    private final InitializableBasketService allBasketServices;
    private final InitializablePictureService pictureService;

    public AppInit(InitializableUserService allServices, List<InitializableProductService> allProductServices,
                   InitializableBasketService allBasketServices,
                   InitializablePictureService pictureService) {
        this.allUserServices = allServices;
        this.allProductServices = allProductServices;
        this.allBasketServices = allBasketServices;
        this.pictureService = pictureService;
    }

    @PostConstruct
    public void beginInit() {
        this.allUserServices.init();  //1 init method
        this.pictureService.init();  //1 init method
        this.allProductServices.forEach(srvc -> srvc.init()); //many independent not in any order inits methods of eacg product
        this.allBasketServices.init();  //1 init method
    }
}
