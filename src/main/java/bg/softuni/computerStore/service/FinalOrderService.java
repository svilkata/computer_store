package bg.softuni.computerStore.service;

import bg.softuni.computerStore.init.InitializableFinalOrderService;
import bg.softuni.computerStore.model.entity.orders.BasketOrderEntity;
import bg.softuni.computerStore.model.entity.orders.FinalOrderEntity;
import bg.softuni.computerStore.model.entity.users.ClientExtraInfoEntity;
import bg.softuni.computerStore.model.enums.OrderStatusEnum;
import bg.softuni.computerStore.repository.orders.FinalOrderRepository;
import bg.softuni.computerStore.repository.users.ClientExtraInfoRepository;
import org.springframework.stereotype.Service;

@Service
public class FinalOrderService implements InitializableFinalOrderService {
    private final FinalOrderRepository finalOrderRepository;
    private final BasketService basketService;
    private final ClientExtraInfoRepository clientExtraInfoRepository;

    public FinalOrderService(FinalOrderRepository finalOrderRepository, BasketService basketService, ClientExtraInfoRepository clientExtraInfoRepository) {
        this.finalOrderRepository = finalOrderRepository;
        this.basketService = basketService;
        this.clientExtraInfoRepository = clientExtraInfoRepository;
    }

    @Override
    public void init() {
        if (finalOrderRepository.count() == 0) {
            processOrder(1L);
        }
    }

    public void processOrder(Long basketId) {
        BasketOrderEntity basketOrder = this.basketService.readOneBasket(basketId);
        ClientExtraInfoEntity clientExtraInfoEntity = new ClientExtraInfoEntity()
                .setDeliveryAddress("Ivan Rilski 5, Sofia")
                .setPhoneNumber("0898822977")
                .setExtraNotes("bla bla bla bla bla")
                .setUser(basketOrder.getUser());

        clientExtraInfoEntity = this.clientExtraInfoRepository.save(clientExtraInfoEntity);

        FinalOrderEntity finalOrderEntity = new FinalOrderEntity();
        finalOrderEntity
                .setProducts(basketOrder.getProducts())
                .setUser(basketOrder.getUser());

        //TODO we wait the client to enter his extra info details for the current order
        finalOrderEntity.setExtraInfoForCurrentOrder(clientExtraInfoEntity);
        //TODO we wait the client to finally confirm the current order
        finalOrderEntity.setStatus(OrderStatusEnum.CONFIRMED_BY_CUSTOMER);


        //AFter a save, the UUID is generated automatically
//        finalOrderEntity.setId(UUID.randomUUID());
        FinalOrderEntity save = this.finalOrderRepository.save(finalOrderEntity);
        this.basketService.deleteOneBasket(basketId);
    }
}
