package bg.softuni.computerStore.service;


import bg.softuni.computerStore.init.InitializableBasketService;
import bg.softuni.computerStore.model.entity.orders.BasketOrderEntity;
import bg.softuni.computerStore.model.entity.products.ItemEntity;
import bg.softuni.computerStore.model.entity.users.UserEntity;
import bg.softuni.computerStore.repository.orders.BasketRepository;
import bg.softuni.computerStore.repository.products.AllItemsRepository;
import bg.softuni.computerStore.repository.users.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BasketService implements InitializableBasketService {
    private final BasketRepository basketRepository;
    private final UserRepository userRepository;
    private final AllItemsRepository allItemsRepository;

    public BasketService(BasketRepository basketRepository, UserRepository userRepository, AllItemsRepository allItemsRepository) {
        this.basketRepository = basketRepository;
        this.userRepository = userRepository;
        this.allItemsRepository = allItemsRepository;
    }

    @Override
    public void init() {
        if (basketRepository.count() == 0) {
            basketInit();
        } else {
            readOneBasket();
        }
    }


    private void basketInit() {
        UserEntity admin = userRepository.findByUsername("admin").orElseThrow();
        List<ItemEntity> allItemsInTheCurrentBasket = this.allItemsRepository.findAll();

        BasketOrderEntity basketOrder = new BasketOrderEntity()
                .setUser(admin)
                .setProducts(allItemsInTheCurrentBasket);

        basketRepository.save(basketOrder);
    }

    private void readOneBasket() {
        BasketOrderEntity basketOrder1 = this.basketRepository.findById(1L).orElseThrow();
    }
}
