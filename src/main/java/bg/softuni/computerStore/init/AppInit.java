package bg.softuni.computerStore.init;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

@Component
public class AppInit {
    //Всички service класове, които сме имплементирали с InitializableService interface,
    // тук ни се зареждат автоматично - Open-Close principle
    private final List<InitializableUserService> allUserServices;
    private final List<InitializableProductService> allProductServices;
    private final List<InitializableBasketService> allBasketServices;

    public AppInit(List<InitializableUserService> allServices, List<InitializableProductService> allProductServices, List<InitializableBasketService> allBasketServices) {
        this.allUserServices = allServices;
        this.allProductServices = allProductServices;
        this.allBasketServices = allBasketServices;
    }

    @PostConstruct
    public void beginInit() {
        this.allUserServices.forEach(srvc -> srvc.init());  //1 init method
        this.allProductServices.forEach(srvc -> srvc.init()); //many independent not in any order inits methods of eacg product
        this.allBasketServices.forEach(srvc -> srvc.init());  //1 init method
    }
}
