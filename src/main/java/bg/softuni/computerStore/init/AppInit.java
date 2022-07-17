package bg.softuni.computerStore.init;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

@Component
public class AppInit {
    //Всички service класове, които сме имплементирали с InitializableService interface,
    // тук ни се зареждат автоматично - Open-Close principle
    private final List<InitializableService> allServices;

    public AppInit(List<InitializableService> allServices) {
        this.allServices = allServices;
    }

    @PostConstruct
    public void beginInit() {
        this.allServices.forEach(srvc -> srvc.init());
    }
}
