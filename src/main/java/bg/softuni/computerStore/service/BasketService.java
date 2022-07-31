package bg.softuni.computerStore.service;


import bg.softuni.computerStore.initSeed.InitializableBasketService;
import bg.softuni.computerStore.model.entity.orders.BasketOrderEntity;
import bg.softuni.computerStore.model.entity.orders.ItemQuantityInBasketEntity;
import bg.softuni.computerStore.model.entity.products.ItemEntity;
import bg.softuni.computerStore.model.entity.users.UserEntity;
import bg.softuni.computerStore.model.enums.BasketStatus;
import bg.softuni.computerStore.model.view.order.OneBasketViewModel;
import bg.softuni.computerStore.model.view.order.OneItemInBasketViewModel;
import bg.softuni.computerStore.repository.orders.BasketRepository;
import bg.softuni.computerStore.repository.orders.QuantitiesItemsInBasketRepository;
import bg.softuni.computerStore.repository.products.AllItemsRepository;
import bg.softuni.computerStore.repository.users.UserRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BasketService implements InitializableBasketService {
    private final BasketRepository basketRepository;
    private final UserRepository userRepository;
    private final AllItemsRepository allItemsRepository;
    private final QuantitiesItemsInBasketRepository quantitiesItemsInBasketRepository;


    public BasketService(BasketRepository basketRepository, UserRepository userRepository, AllItemsRepository allItemsRepository, QuantitiesItemsInBasketRepository quantitiesItemsInBasketRepository) {
        this.basketRepository = basketRepository;
        this.userRepository = userRepository;
        this.allItemsRepository = allItemsRepository;

        this.quantitiesItemsInBasketRepository = quantitiesItemsInBasketRepository;
    }

    @Override
    public void init() {
        if (basketRepository.count() == 0) {
            basketsInit();
        }
    }

    private void basketsInit() {
        List<ItemEntity> allItemsInTheCurrentBasket = this.allItemsRepository.findAll();

        //Basket 1
        UserEntity admin = userRepository.findByUsername("admin").orElseThrow();
        List<ItemEntity> basketOrder1Items = allItemsInTheCurrentBasket.subList(0, 3);
        BasketOrderEntity basketOrder1 = new BasketOrderEntity()
                .setUser(admin)
                .setProducts(basketOrder1Items)
                .setBasketStatus(BasketStatus.OPEN)
                .setCreationDateTime(LocalDateTime.now());
        this.basketRepository.save(basketOrder1);
        initItemQuantityInBasket(basketOrder1, 2);


        //Basket 2
        UserEntity purchase = userRepository.findByUsername("purchase").orElseThrow();
        List<ItemEntity> basketOrder2Items = allItemsInTheCurrentBasket.subList(6, 8);
        BasketOrderEntity basketOrder2 = new BasketOrderEntity()
                .setUser(purchase)
                .setProducts(basketOrder2Items)
                .setBasketStatus(BasketStatus.OPEN)
                .setCreationDateTime(LocalDateTime.now());
        this.basketRepository.save(basketOrder2);
        initItemQuantityInBasket(basketOrder2, 1);

        //Basket 3
        UserEntity sales = userRepository.findByUsername("sales").orElseThrow();
        List<ItemEntity> basketOrder3Items = allItemsInTheCurrentBasket.subList(3, 6);
        BasketOrderEntity basketOrder3 = new BasketOrderEntity()
                .setUser(sales)
                .setProducts(basketOrder3Items)
                .setBasketStatus(BasketStatus.OPEN)
                .setCreationDateTime(LocalDateTime.now());
        this.basketRepository.save(basketOrder3);
        initItemQuantityInBasket(basketOrder3, 2);


        //Basket 4
        UserEntity customer = userRepository.findByUsername("customer").orElseThrow();
        List<ItemEntity> basketOrder4Items = allItemsInTheCurrentBasket.subList(5, 7);
        BasketOrderEntity basketOrder4 = new BasketOrderEntity()
                .setUser(customer)
                .setProducts(basketOrder4Items)
                .setBasketStatus(BasketStatus.OPEN)
                .setCreationDateTime(LocalDateTime.now());
        this.basketRepository.save(basketOrder4);


        //==>>
        //TODO still testing here
        for (ItemEntity basketOrder4OneItem : basketOrder4Items) {
            ItemQuantityInBasketEntity rec = new ItemQuantityInBasketEntity();
            int boughtQuantity = 2;
            int orderedQuantity = checkBeginningAvailableQuantityOfItem(basketOrder4OneItem, boughtQuantity);
            rec
                    .setBasket(basketOrder4)
                    .setItem(basketOrder4OneItem)
                    .setQuantityBought(orderedQuantity);

            this.quantitiesItemsInBasketRepository.save(rec);
        }

        //Changing item quantity in the Basket with new quantity in basket from that item
        deductItemQuantityInBasket(basketOrder4, basketOrder4Items.get(0), 1);

        //TODO Increasing Item quantity in a basket
        // <<==
    }

    private void initItemQuantityInBasket(BasketOrderEntity basketOrder, int eachItemQtity) {
        for (ItemEntity basketOrder1OneItem : basketOrder.getProducts()) {
            ItemQuantityInBasketEntity rec = new ItemQuantityInBasketEntity();
            int orderedQuantity = checkBeginningAvailableQuantityOfItem(basketOrder1OneItem, eachItemQtity);
            rec
                    .setBasket(basketOrder)
                    .setItem(basketOrder1OneItem)
                    .setQuantityBought(orderedQuantity);

            this.quantitiesItemsInBasketRepository.save(rec);
        }
    }

    private void deductItemQuantityInBasket(BasketOrderEntity basketOrder,
                                            ItemEntity basketOrderOnеItem,
                                            int newQtityOfItemInBasket) {
        if (basketOrderOnеItem.getCurrentQuantity() == newQtityOfItemInBasket) {
            return;
        }

        if (basketOrderOnеItem.getCurrentQuantity() > newQtityOfItemInBasket) {
            //We deduct the bought quantity and increase the available quantity
            ItemQuantityInBasketEntity rec =
                    getItemFromItemQuantityInBasketEntityByBasketItem(basketOrder, basketOrderOnеItem);

            int deductedQuantity = rec.getQuantityBought() - newQtityOfItemInBasket;

            rec.setQuantityBought(newQtityOfItemInBasket);
            this.quantitiesItemsInBasketRepository.save(rec);

            basketOrderOnеItem.setCurrentQuantity(basketOrderOnеItem.getCurrentQuantity() + deductedQuantity);
            this.allItemsRepository.save(basketOrderOnеItem);
        }
    }

    public ItemQuantityInBasketEntity getItemFromItemQuantityInBasketEntityByBasketItem(
            BasketOrderEntity basketOrder, ItemEntity basketOrderOnеItem) {
        return this.quantitiesItemsInBasketRepository.findByBasket_IdAndItem_ItemId(
                basketOrder.getId(),
                basketOrderOnеItem.getItemId());
    }

    private int checkBeginningAvailableQuantityOfItem(ItemEntity basketOrderOnеItem, int boughtQuantity) {
        if (basketOrderOnеItem.getCurrentQuantity() > boughtQuantity) {
            basketOrderOnеItem.setCurrentQuantity(basketOrderOnеItem.getCurrentQuantity() - boughtQuantity);
            this.allItemsRepository.save(basketOrderOnеItem);
            return boughtQuantity;
        } else {
            int availableQtity = basketOrderOnеItem.getCurrentQuantity();
            basketOrderOnеItem.setCurrentQuantity(0);
            this.allItemsRepository.save(basketOrderOnеItem);
            return availableQtity;
        }
    }

    public BasketOrderEntity readOneBasket(Long basketId) {
        return this.basketRepository.findBasketById(basketId).orElseThrow();
    }

    public Long getBaskeIdByUserId(Long userId) {
        return this.basketRepository.findBasketIdByUserId(userId);
    }


    @Transactional
    public void resetOneBasket(Long basketId) {
        this.quantitiesItemsInBasketRepository.deleteAllByBasket_Id(basketId);
        BasketOrderEntity basketToReset = this.basketRepository.findBasketById(basketId).orElseThrow();
        basketToReset.setProducts(new ArrayList<>());
        basketToReset.setBasketStatus(BasketStatus.CLOSED);
        this.basketRepository.save(basketToReset);
    }

    public OneBasketViewModel viewAllItemsFromOneBasket(Long basketId) {
        BasketOrderEntity basketOrder = readOneBasket(basketId);
        List<OneItemInBasketViewModel> itemsInBasketView = new ArrayList<>();
        BigDecimal totalValueItems = new BigDecimal(0);

        List<ItemEntity> productsInCurrentBasket = basketOrder.getProducts();
        for (ItemEntity itemEntity : productsInCurrentBasket) {
            OneItemInBasketViewModel oneItemInBasketViewModel = new OneItemInBasketViewModel();
            oneItemInBasketViewModel
                    .setItemId(itemEntity.getItemId())
                    .setModel(itemEntity.getModel())
                    .setType(itemEntity.getType())
                    .setPhotoUrl(itemEntity.getPhoto().getUrl());

            ItemQuantityInBasketEntity rec =
                    getItemFromItemQuantityInBasketEntityByBasketItem(basketOrder, itemEntity);

            int quantityBought = rec.getQuantityBought();
            oneItemInBasketViewModel.setQuantity(quantityBought);

            BigDecimal currentValue = itemEntity.getSellingPrice().multiply(BigDecimal.valueOf(quantityBought));
            oneItemInBasketViewModel.setSellingPriceForAllQuantity(currentValue);

            totalValueItems = totalValueItems.add(currentValue);

            itemsInBasketView.add(oneItemInBasketViewModel);
        }

        OneBasketViewModel basketViewModel = new OneBasketViewModel();
        basketViewModel
                .setItems(itemsInBasketView)
                .setTotalValue(totalValueItems);

        return basketViewModel;
    }
}
