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

    public AppInit(InitializableUserService allServices, List<InitializableProductService> allProductServices,
                   InitializableBasketService allBasketServices) {
        this.allUserServices = allServices;
        this.allProductServices = allProductServices;
        this.allBasketServices = allBasketServices;
    }

    @PostConstruct
    public void beginInit() {
        this.allUserServices.init();  //1 init method
        this.allProductServices.forEach(srvc -> srvc.init()); //many independent not in any order inits methods of eacg product
        this.allBasketServices.init();  //1 init method
    }
}
