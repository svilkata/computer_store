package bg.softuni.computerStore.service;

import bg.softuni.computerStore.event.OrderCreatedEvent;
import bg.softuni.computerStore.exception.OrderNotFoundException;
import bg.softuni.computerStore.initSeed.InitializableFinalOrderService;
import bg.softuni.computerStore.model.binding.order.ClientOrderExtraInfoGetViewModel;
import bg.softuni.computerStore.model.entity.orders.BasketEntity;
import bg.softuni.computerStore.model.entity.orders.FinalOrderEntity;
import bg.softuni.computerStore.model.entity.orders.ItemQuantityInBasketEntity;
import bg.softuni.computerStore.model.entity.orders.ItemQuantityInOrderEntity;
import bg.softuni.computerStore.model.entity.products.ItemEntity;
import bg.softuni.computerStore.model.entity.orders.ClientOrderExtraInfoEntity;
import bg.softuni.computerStore.model.enums.OrderStatusEnum;
import bg.softuni.computerStore.repository.orders.FinalOrderRepository;
import bg.softuni.computerStore.repository.orders.QuantitiesItemsInOrderRepository;
import bg.softuni.computerStore.repository.users.ClientExtraInfoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class FinalOrderService implements InitializableFinalOrderService {
    private final FinalOrderRepository finalOrderRepository;
    private final BasketService basketService;
    private final ClientExtraInfoRepository clientExtraInfoRepository;
    private final QuantitiesItemsInOrderRepository quantitiesItemsInOrderRepository;

    private static final Logger LOGGER = LoggerFactory.getLogger(FinalOrderService.class);
    private final ApplicationEventPublisher eventPublisher;

    public FinalOrderService(FinalOrderRepository finalOrderRepository, BasketService basketService, ClientExtraInfoRepository clientExtraInfoRepository, QuantitiesItemsInOrderRepository quantitiesItemsInOrderRepository, ApplicationEventPublisher eventPublisher) {
        this.finalOrderRepository = finalOrderRepository;
        this.basketService = basketService;
        this.clientExtraInfoRepository = clientExtraInfoRepository;
        this.quantitiesItemsInOrderRepository = quantitiesItemsInOrderRepository;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public void init() {
        if (finalOrderRepository.count() == 0) {
            ClientOrderExtraInfoGetViewModel clientOrderExtraInfoGetViewModel = new ClientOrderExtraInfoGetViewModel();
            clientOrderExtraInfoGetViewModel
                    .setDeliveryAddress("Ivan Rilski 5, Sofia")
                    .setPhoneNumber("0898822977")
                    .setExtraNotes("bla bla bla bla bla");
            processOrder(2L, clientOrderExtraInfoGetViewModel, BigDecimal.valueOf(626.43));
            processOrder(3L, clientOrderExtraInfoGetViewModel, BigDecimal.valueOf(6337.38));
        } else {
//            confirmOrderByStore(UUID_ORDER_NUMBER_TESTING);
//            markOrderAsDelivered(UUID_ORDER_NUMBER_TESTING);
        }
    }

    @Transactional
    @Modifying
    public String processOrder(Long basketId, ClientOrderExtraInfoGetViewModel clientOrderExtraInfoGetViewModel,
                               BigDecimal totalTotal) {
        LOGGER.info("Starting creating order from basket with id: {}", basketId);

        BasketEntity basketOrder = this.basketService.readOneBasket(basketId);
        ClientOrderExtraInfoEntity clientOrderExtraInfoEntity = new ClientOrderExtraInfoEntity()
                .setDeliveryAddress(clientOrderExtraInfoGetViewModel.getDeliveryAddress())
                .setPhoneNumber(clientOrderExtraInfoGetViewModel.getPhoneNumber())
                .setExtraNotes(clientOrderExtraInfoGetViewModel.getExtraNotes())
                .setUser(basketOrder.getUser());

        clientOrderExtraInfoEntity = this.clientExtraInfoRepository.save(clientOrderExtraInfoEntity);

        List<ItemEntity> products = basketOrder.getProducts();

        FinalOrderEntity finalOrderEntity = new FinalOrderEntity();
        finalOrderEntity
                .setProducts(products)
                .setTotalTotal(totalTotal)
                .setUser(basketOrder.getUser());

        finalOrderEntity.setExtraInfoForCurrentOrder(clientOrderExtraInfoEntity);
        finalOrderEntity.setStatus(OrderStatusEnum.CONFIRMED_BY_CUSTOMER);

        //Giving random number for orderNumber
        finalOrderEntity.setOrderNumber(UUID.randomUUID().toString());

        finalOrderEntity.setCreationDateTime(LocalDateTime.now());

        //We do not set in advance the @Id of UUID for the current order,
        // because after a save, the id UUID is generated automatically

        int countOfProducts = 0;

        List<ItemQuantityInOrderEntity> prepItemsQuantities = new ArrayList<>();
        //Then PREPARATION from basket tables saving quantities to table orders_item_quantity
        for (ItemEntity basketOneItem : basketOrder.getProducts()) {
            ItemQuantityInOrderEntity rec = new ItemQuantityInOrderEntity();

            ItemQuantityInBasketEntity itemFromItemQuantityInBasketEntityByBasketItem = this.basketService.getItemFromItemQuantityInBasketEntityByBasketItem(
                    basketOrder, basketOneItem);

            int quantityBought = itemFromItemQuantityInBasketEntityByBasketItem.getQuantityBought();

            rec
                    .setItem(basketOneItem)
                    .setBoughtQuantity(quantityBought);

            countOfProducts += quantityBought;

            prepItemsQuantities.add(rec);
        }


        //Saving the final confirmed order
        finalOrderEntity.setCountTotalProducts(countOfProducts);
        FinalOrderEntity savedFinalOrder = this.finalOrderRepository.save(finalOrderEntity);


        //Then from basket tables saving quantities to table orders_item_quantity
        for (ItemQuantityInOrderEntity prepItemsQuantity : prepItemsQuantities) {
            prepItemsQuantity.setOrder(savedFinalOrder);
            this.quantitiesItemsInOrderRepository.save(prepItemsQuantity);
        }

        //Finally, we delete/reset the info in all 3 basket tables
        this.basketService.resetOneBasketWhenFinalOrderConfirmed(basketId);

        //first creating the event
        OrderCreatedEvent orderCreatedEvent = new OrderCreatedEvent(
                FinalOrderService.class.getSimpleName(),
                savedFinalOrder.getId().toString()
        );

        //then we publish the event
        eventPublisher.publishEvent(orderCreatedEvent);

        return savedFinalOrder.getOrderNumber();
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

    //`iqo` abbreviation comes from ItemQuantityInOrderEntity
    // uuid here is order.getId()
    public List<ItemQuantityInOrderEntity> findIQOUUIDPrimary(UUID uuid) {
        List<ItemQuantityInOrderEntity> allByUUIDPrimary = this.quantitiesItemsInOrderRepository.findAllByUUIDPrimary(uuid);
        return allByUUIDPrimary;
    }

    public FinalOrderEntity getOrderByOrderNumber(String orderNumber) {
        return this.finalOrderRepository.findByOrderNumber(orderNumber)
                .orElseThrow(() -> new OrderNotFoundException(String.format("A final order number: %s  does not exist", orderNumber)));
    }

    public List<ItemQuantityInOrderEntity> getProductQuantitiesFromOrderByOrderNumber(String orderNumber) {
        return this.quantitiesItemsInOrderRepository.findAllByOrder_OrderNumber(orderNumber);
    }

    public List<FinalOrderEntity> getAllOrdersByUserId(Long userId) {
        return this.finalOrderRepository.findAllByUserId(userId);

    }

    public List<FinalOrderEntity> searchAllOrdersLazyByOrderNumber(String searchByOrderNumber) {
        return this.finalOrderRepository.findAllOrdersLazyByOrderNumber(searchByOrderNumber);
    }

    public List<FinalOrderEntity> searchAllOrdersByUserIdAndOrderNumber(Long userId, String searchByOrderNumber) {
        return this.finalOrderRepository.findAllOrdersLazyByUserIdAndOrderNumber(userId, searchByOrderNumber);
    }
}
