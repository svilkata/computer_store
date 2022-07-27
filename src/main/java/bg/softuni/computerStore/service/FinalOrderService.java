package bg.softuni.computerStore.service;

import bg.softuni.computerStore.initSeed.InitializableFinalOrderService;
import bg.softuni.computerStore.model.entity.orders.BasketOrderEntity;
import bg.softuni.computerStore.model.entity.orders.FinalOrderEntity;
import bg.softuni.computerStore.model.entity.orders.ItemQuantityInBasketEntity;
import bg.softuni.computerStore.model.entity.orders.ItemQuantityInOrderEntity;
import bg.softuni.computerStore.model.entity.products.ItemEntity;
import bg.softuni.computerStore.model.entity.orders.ClientOrderExtraInfoEntity;
import bg.softuni.computerStore.model.enums.OrderStatusEnum;
import bg.softuni.computerStore.repository.orders.FinalOrderRepository;
import bg.softuni.computerStore.repository.orders.QuantitiesItemsInOrderRepository;
import bg.softuni.computerStore.repository.users.ClientExtraInfoRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class FinalOrderService implements InitializableFinalOrderService {
    private final FinalOrderRepository finalOrderRepository;
    private final BasketService basketService;
    private final ClientExtraInfoRepository clientExtraInfoRepository;
    private final QuantitiesItemsInOrderRepository quantitiesItemsInOrderRepository;

    public FinalOrderService(FinalOrderRepository finalOrderRepository, BasketService basketService, ClientExtraInfoRepository clientExtraInfoRepository, QuantitiesItemsInOrderRepository quantitiesItemsInOrderRepository) {
        this.finalOrderRepository = finalOrderRepository;
        this.basketService = basketService;
        this.clientExtraInfoRepository = clientExtraInfoRepository;
        this.quantitiesItemsInOrderRepository = quantitiesItemsInOrderRepository;
    }

    @Override
    public void init() {
        if (finalOrderRepository.count() == 0) {
            processOrder(2L);
            processOrder(3L);
        } else {
//            confirmOrderByStore(UUID_ORDER_NUMBER_TESTING);
//            markOrderAsDelivered(UUID_ORDER_NUMBER_TESTING);
        }
    }

    public void processOrder(Long basketId) {
        BasketOrderEntity basketOrder = this.basketService.readOneBasket(basketId);
        ClientOrderExtraInfoEntity clientOrderExtraInfoEntity = new ClientOrderExtraInfoEntity()
                .setDeliveryAddress("Ivan Rilski 5, Sofia")
                .setPhoneNumber("0898822977")
                .setExtraNotes("bla bla bla bla bla")
                .setUser(basketOrder.getUser());

        clientOrderExtraInfoEntity = this.clientExtraInfoRepository.save(clientOrderExtraInfoEntity);

        FinalOrderEntity finalOrderEntity = new FinalOrderEntity();
        finalOrderEntity
                .setProducts(basketOrder.getProducts())
                .setUser(basketOrder.getUser());

        //TODO we wait the client to enter his extra info details for the current order
        finalOrderEntity.setExtraInfoForCurrentOrder(clientOrderExtraInfoEntity);
        //TODO we wait the client to finally confirm the current order
        finalOrderEntity.setStatus(OrderStatusEnum.CONFIRMED_BY_CUSTOMER);

        //Giving random number for orderNumber
        finalOrderEntity.setOrderNumber(UUID.randomUUID().toString());

        //We do not set in advance the @Id of UUID for the current order,
        // because after a save, the id UUID is generated automatically

        //Saving the final confirmed order
        FinalOrderEntity savedFinalOrder = this.finalOrderRepository.save(finalOrderEntity.setCreationDateTime(LocalDateTime.now()));

        //Then from basket tables saving quantities to table orders_item_quantity
        List<ItemEntity> order1Items = savedFinalOrder.getProducts();
        for (ItemEntity basketOneItem : basketOrder.getProducts()) {
            ItemQuantityInOrderEntity rec = new ItemQuantityInOrderEntity();

            ItemQuantityInBasketEntity itemFromItemQuantityInBasketEntityByBasketItem = this.basketService.getItemFromItemQuantityInBasketEntityByBasketItem(
                    basketOrder, basketOneItem);

            rec
                    .setOrder(savedFinalOrder)
                    .setItem(basketOneItem)
                    .setBoughtQuantity(itemFromItemQuantityInBasketEntityByBasketItem.getQuantityBought());

            this.quantitiesItemsInOrderRepository.save(rec);
        }

        //Finally, we delete all info in all 3 basket tables
        this.basketService.deleteOneBasket(basketId);
    }

    public void confirmOrderByStore(String orderNumber) {
        //It also works:
//        UUID uuid
//        Optional<FinalOrderEntity> currentOrder = this.finalOrderRepository.findById(uuid);

        //The lazy way
        FinalOrderEntity currentOrder = this.finalOrderRepository.findByOrderNumber(orderNumber).orElseThrow();
        currentOrder.setStatus(OrderStatusEnum.CONFIRMED_BY_STORE);

        this.finalOrderRepository.save(currentOrder);
    }

    public void markOrderAsDelivered(String orderNumber) {
        UUID uuid = this.finalOrderRepository.findByOrderNumber(orderNumber).get().getId();

        //The eager way
        FinalOrderEntity currentOrder = this.finalOrderRepository
                .findByOrderNumberByUUIDPrimaryEager(uuid).orElseThrow();
        currentOrder.setStatus(OrderStatusEnum.DELIVERED);

        this.finalOrderRepository.save(currentOrder);
    }

    public List<FinalOrderEntity> getAllOrdersEager() {
        return this.finalOrderRepository.findAllOrderAndItemsEager();
    }

    public List<FinalOrderEntity> getAllOrdersLazy() {
        return this.finalOrderRepository.findAllOrdersLazy();
    }

    //iqo from ItemQuantityInOrderEntity
    public List<ItemQuantityInOrderEntity> findIQO(UUID uuid){
        List<ItemQuantityInOrderEntity> allByUUIDPrimary = this.quantitiesItemsInOrderRepository.findAllByUUIDPrimary(uuid);
        return allByUUIDPrimary;
    }


}
