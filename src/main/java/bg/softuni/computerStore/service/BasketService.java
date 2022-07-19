package bg.softuni.computerStore.service;

import bg.softuni.computerStore.init.InitializableBasketService;
import bg.softuni.computerStore.model.entity.orders.BasketOrderEntity;
import bg.softuni.computerStore.model.entity.products.ComputerEntity;
import bg.softuni.computerStore.model.entity.products.ItemEntity;
import bg.softuni.computerStore.model.entity.products.MonitorEntity;
import bg.softuni.computerStore.model.entity.users.UserEntity;
import bg.softuni.computerStore.repository.orders.BasketRepository;
import bg.softuni.computerStore.repository.products.ComputerRepository;
import bg.softuni.computerStore.repository.products.MonitorRepository;
import bg.softuni.computerStore.repository.users.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BasketService implements InitializableBasketService {
    private final BasketRepository basketRepository;
    private final UserRepository userRepository;
    private final ComputerRepository computerRepository;
    private final MonitorRepository monitorRepository;

    public BasketService(BasketRepository basketRepository, UserRepository userRepository, ComputerRepository computerRepository, MonitorRepository monitorRepository) {
        this.basketRepository = basketRepository;
        this.userRepository = userRepository;
        this.computerRepository = computerRepository;
        this.monitorRepository = monitorRepository;
    }

    @Override
    public void init() {
        if (basketRepository.count() == 0) {
            basketInit();
        } else {
            readBaskets();
        }
    }


    private void basketInit() {
        UserEntity admin = userRepository.findByUsername("admin").orElseThrow();
        List<ComputerEntity> allComputers = computerRepository.findAll();
        List<MonitorEntity> allMonitors = monitorRepository.findAll();
        List<ItemEntity> allItemsInTheCurrentBasket = new ArrayList<>();
        allItemsInTheCurrentBasket.addAll(allComputers);
        allItemsInTheCurrentBasket.addAll(allMonitors);

        BasketOrderEntity basketOrder = new BasketOrderEntity()
                .setUser(admin)
                .setProducts(allItemsInTheCurrentBasket);

        basketRepository.save(basketOrder);
    }

    private void readBaskets() {
        BasketOrderEntity basketOrder1 = this.basketRepository.findById(1L).orElseThrow();
    }
}
