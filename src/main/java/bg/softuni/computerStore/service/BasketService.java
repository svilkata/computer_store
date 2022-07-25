package bg.softuni.computerStore.service;


import bg.softuni.computerStore.init.InitializableBasketService;
import bg.softuni.computerStore.model.entity.orders.BasketOrderEntity;
import bg.softuni.computerStore.model.entity.products.ItemEntity;
import bg.softuni.computerStore.model.entity.users.UserEntity;
import bg.softuni.computerStore.model.enums.BasketStatus;
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
            basketsInit();
        }
    }


    private void basketsInit() {
        UserEntity admin = userRepository.findByUsername("admin").orElseThrow();
        List<ItemEntity> allItemsInTheCurrentBasket = this.allItemsRepository.findAll();

        BasketOrderEntity basketOrder1 = new BasketOrderEntity()
                .setUser(admin)
                .setProducts(allItemsInTheCurrentBasket.subList(0, 3))
                .setBasketStatus(BasketStatus.OPEN);
        this.basketRepository.save(basketOrder1);

        UserEntity customer = userRepository.findByUsername("customer").orElseThrow();
        BasketOrderEntity basketOrder2 = new BasketOrderEntity()
                .setUser(customer)
                .setProducts(allItemsInTheCurrentBasket.subList(3, 5))
                .setBasketStatus(BasketStatus.OPEN);
        this.basketRepository.save(basketOrder2);

    }

    public BasketOrderEntity readOneBasket(Long basketId) {
        BasketOrderEntity basketOrder1 = this.basketRepository.findBasketById(basketId).orElseThrow();
        return basketOrder1;
//        BasketOrderEntity basketOrder2 = this.basketRepository.findById(2L).orElseThrow();
    }

    public void deleteOneBasket(Long basketId) {
        this.basketRepository.deleteById(basketId);
    }
}
