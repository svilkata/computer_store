package bg.softuni.computerStore.service;


import bg.softuni.computerStore.initSeed.InitializableBasketService;
import bg.softuni.computerStore.model.entity.orders.BasketEntity;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class BasketService implements InitializableBasketService {
    private final BasketRepository basketRepository;
    private final UserRepository userRepository;
    private final AllItemsRepository allItemsRepository;
    private final QuantitiesItemsInBasketRepository quantitiesItemsInBasketRepository;
    private static final Logger LOGGER = LoggerFactory.getLogger(BasketService.class);

    public BasketService(BasketRepository basketRepository, UserRepository userRepository, AllItemsRepository allItemsRepository, QuantitiesItemsInBasketRepository quantitiesItemsInBasketRepository) {
        this.basketRepository = basketRepository;
        this.userRepository = userRepository;
        this.allItemsRepository = allItemsRepository;

        this.quantitiesItemsInBasketRepository = quantitiesItemsInBasketRepository;
    }

    @Override
    @Transactional
    public void init() {
        if (basketRepository.count() == 0) {
            basketsInit();
        }
    }

    private void basketsInit() {
        List<ItemEntity> allItemsInTheCurrentBasket = (List<ItemEntity>) this.allItemsRepository.findAll();

        //Basket 1
        UserEntity admin = userRepository.findByUsername("admin").orElseThrow();
        List<ItemEntity> basketOrder1Items = allItemsInTheCurrentBasket.subList(0, 3);
        BasketEntity basketOrder1 = new BasketEntity()
                .setUser(admin)
                .setProducts(basketOrder1Items)
                .setBasketStatus(BasketStatus.OPEN)
                .setCreationDateTime(LocalDateTime.now());
        this.basketRepository.save(basketOrder1);
        seedingInitialItemQuantityInBasket(basketOrder1, 2);

        //Basket 2
        UserEntity purchase = userRepository.findByUsername("purchase").orElseThrow();
        List<ItemEntity> basketOrder2Items = allItemsInTheCurrentBasket.subList(8, 9);
        BasketEntity basketOrder2 = new BasketEntity()
                .setUser(purchase)
                .setProducts(basketOrder2Items)
                .setBasketStatus(BasketStatus.OPEN)
                .setCreationDateTime(LocalDateTime.now());
        this.basketRepository.save(basketOrder2);
        seedingInitialItemQuantityInBasket(basketOrder2, 1);


        //Basket 3
        UserEntity sales = userRepository.findByUsername("sales").orElseThrow();
        List<ItemEntity> basketOrder3Items = allItemsInTheCurrentBasket.subList(2, 3);
        BasketEntity basketOrder3 = new BasketEntity()
                .setUser(sales)
                .setProducts(basketOrder3Items)
                .setBasketStatus(BasketStatus.OPEN)
                .setCreationDateTime(LocalDateTime.now());
        this.basketRepository.save(basketOrder3);
        seedingInitialItemQuantityInBasket(basketOrder3, 2);


        //Basket 4
        UserEntity customer = userRepository.findByUsername("customer").orElseThrow();
        List<ItemEntity> basketOrder4Items = allItemsInTheCurrentBasket.subList(5, 7);
        BasketEntity basketOrder4 = new BasketEntity()
                .setUser(customer)
                .setProducts(basketOrder4Items)
                .setBasketStatus(BasketStatus.OPEN)
                .setCreationDateTime(LocalDateTime.now());
        this.basketRepository.save(basketOrder4);

        //==>>
        for (ItemEntity basketOrder4OneItem : basketOrder4Items) {
            ItemQuantityInBasketEntity rec = new ItemQuantityInBasketEntity();
            int boughtQuantity = 1;
            int orderedQuantity = checkBeginningAvailableQuantityOfItem(basketOrder4OneItem, boughtQuantity);
            rec
                    .setBasket(basketOrder4)
                    .setItem(basketOrder4OneItem)
                    .setQuantityBought(orderedQuantity);

            this.quantitiesItemsInBasketRepository.save(rec);
        }

        //Changing item quantity in the Basket with new quantity in basket from that item
        changeItemQuantityInBasket(basketOrder4, basketOrder4Items.get(0), 1);
    }

    private void seedingInitialItemQuantityInBasket(BasketEntity basketOrder, int eachItemQtity) {
        for (ItemEntity basketOrderOneItem : basketOrder.getProducts()) {
            ItemQuantityInBasketEntity rec = new ItemQuantityInBasketEntity();
            int orderedQuantity = checkBeginningAvailableQuantityOfItem(basketOrderOneItem, eachItemQtity);
            rec
                    .setBasket(basketOrder)
                    .setItem(basketOrderOneItem)
                    .setQuantityBought(orderedQuantity);

            this.quantitiesItemsInBasketRepository.save(rec);
        }
    }

    private int checkBeginningAvailableQuantityOfItem(ItemEntity basketOrderOneItem, int extraBought) {
        if (basketOrderOneItem.getCurrentQuantity() > extraBought) {  //we have available quantity stock
            basketOrderOneItem.setCurrentQuantity(basketOrderOneItem.getCurrentQuantity() - extraBought);
            this.allItemsRepository.save(basketOrderOneItem);
            return extraBought;
        } else {  //we do have available quantity stock
            int availableQtity = basketOrderOneItem.getCurrentQuantity();
            basketOrderOneItem.setCurrentQuantity(0);
            this.allItemsRepository.save(basketOrderOneItem);
            return availableQtity;
        }
    }


//  No transaction here so to assure the whole chain of transactions either will pass or not
    public void resetOneBasketWhenFinalOrderConfirmed(Long basketId) {
        this.quantitiesItemsInBasketRepository.deleteAllByBasket_Id(basketId);
        BasketEntity basketToReset = this.basketRepository.findBasketByIdEager(basketId).orElseThrow();
        basketToReset.setProducts(new ArrayList<ItemEntity>());
        basketToReset.setBasketStatus(BasketStatus.CLOSED);
        basketToReset.setCreationDateTime(null);
        this.basketRepository.save(basketToReset);
    }

    @Transactional
    public int addNewItemToBasket(Long itemId, Long basketId) {
        BasketEntity basketOrder = this.basketRepository.findBasketOrderEntitiesById(basketId).orElseThrow();
//        BasketOrderEntity basketOrder = this.basketRepository.findBasketByIdEager(basketId).orElseThrow();
        ItemEntity itemToAdd = this.allItemsRepository.findById(itemId).orElseThrow();
        if (itemToAdd.getCurrentQuantity() == 0) {
            return -2; //zero quantity
        }

        List<ItemEntity> products = basketOrder.getProducts();
        List<ItemEntity> newProductList = new ArrayList<>();

        if (products.size() == 0) {
            basketOrder.setBasketStatus(BasketStatus.OPEN);
            basketOrder.setCreationDateTime(LocalDateTime.now());
            newProductList.add(itemToAdd);
        } else {
            if (products.stream().map(p -> p.getItemId()).collect(Collectors.toList()).contains(itemToAdd.getItemId())) {
                return -1; //product already added to the basket of the user
            } else {
                for (ItemEntity product : products) {
                    newProductList.add(product);
                }
                newProductList.add(itemToAdd);
            }
        }

        basketOrder.setProducts(newProductList);
        this.basketRepository.save(basketOrder);
        addOneItemToItemQuantity(basketOrder, itemToAdd);

        return 1; //successfully added item in the basket
    }

    private void addOneItemToItemQuantity(BasketEntity basketOrder, ItemEntity itemToAdd) {
        ItemQuantityInBasketEntity rec = new ItemQuantityInBasketEntity();
        int orderedQuantity = checkBeginningAvailableQuantityOfItem(itemToAdd, 1);
        rec
                .setBasket(basketOrder)
                .setItem(itemToAdd)
                .setQuantityBought(orderedQuantity);

        this.quantitiesItemsInBasketRepository.save(rec);
    }


    public OneBasketViewModel viewAllItemsFromOneBasket(Long basketId) {
        BasketEntity basketOrder = getOneBasket(basketId);
        List<OneItemInBasketViewModel> itemsInBasketView = new ArrayList<>();
        BigDecimal totalValueItems = new BigDecimal(0);

        List<ItemEntity> productsInCurrentBasket = basketOrder.getProducts();
        for (ItemEntity itemEntity : productsInCurrentBasket) {
            OneItemInBasketViewModel oneItemInBasketViewModel = new OneItemInBasketViewModel();
            oneItemInBasketViewModel
                    .setItemId(itemEntity.getItemId())
                    .setModel(itemEntity.getModel())
                    .setType(itemEntity.getType())
                    .setPhotoUrl(itemEntity.getPhoto() != null ? itemEntity.getPhoto().getUrl() : "");

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

    public Long getUserIdByBasketId(Long basketId) {
        return this.basketRepository.findUserIdByBasketId(basketId);
    }


    @Transactional
    public int changeOrderedQuantity(Long basketId, Long itemId, Long newQuantity) {
        BasketEntity basketOrder = this.basketRepository.findBasketOrderEntitiesById(basketId).orElseThrow();
        ItemEntity basketOrderOneItem = this.allItemsRepository.findById(itemId).orElseThrow();

        return changeItemQuantityInBasket(basketOrder, basketOrderOneItem, Integer.parseInt(newQuantity + ""));
    }


    private int changeItemQuantityInBasket(BasketEntity basketOrder, ItemEntity basketOrderOneItem,
                                           int newQtityOfItemInBasket) {
        //the item in quantitiesItemsInBasketRepository
        ItemQuantityInBasketEntity currentItemQuantityInTheBasket =
                getItemFromItemQuantityInBasketEntityByBasketItem(basketOrder, basketOrderOneItem);

        int changedQuantity = currentItemQuantityInTheBasket.getQuantityBought() - newQtityOfItemInBasket;

        //when we try to order negative quantity or zero quantity
        if (currentItemQuantityInTheBasket.getQuantityBought() >= 0 && newQtityOfItemInBasket <= 0) {
            return -2; //negative quantity
        }

        //we have not changed the ordered quantity
//        if (changedQuantity == 0) {
//            return -3; //no change in the quantity
//        }

        //we deduct the quantity bought
        if (changedQuantity > 0) {
            //we increase the quantity in allItemsRepository
            basketOrderOneItem.setCurrentQuantity(basketOrderOneItem.getCurrentQuantity() + changedQuantity);
            this.allItemsRepository.save(basketOrderOneItem);
            currentItemQuantityInTheBasket.setQuantityBought(newQtityOfItemInBasket);
            this.quantitiesItemsInBasketRepository.save(currentItemQuantityInTheBasket);
            return 1;
        }

        //we increase the quantity
        if (changedQuantity < 0) {
            //we decrease the quantity in allItemsRepository
            int increasedQuantity = checkBeginningAvailableQuantityOfItem(basketOrderOneItem, -changedQuantity);
            currentItemQuantityInTheBasket.setQuantityBought(currentItemQuantityInTheBasket.getQuantityBought() + increasedQuantity);
            this.quantitiesItemsInBasketRepository.save(currentItemQuantityInTheBasket);
            if (increasedQuantity < -changedQuantity || increasedQuantity == 0) {
                return -1; //last items left
            }
        }

        return 1;
    }

    @Transactional
    @Modifying
    public boolean removeOneItemFromBasket(Long basketId, Long itemId) {
        ItemQuantityInBasketEntity byBasket_idAndItem_itemId = this.quantitiesItemsInBasketRepository.findByBasket_IdAndItem_ItemId(
                basketId, itemId);
        int quantityToIncreaseInStoreStock = byBasket_idAndItem_itemId.getQuantityBought();
        ItemEntity allItemsItem = this.allItemsRepository.findById(itemId).orElseThrow();
        allItemsItem.setCurrentQuantity(allItemsItem.getCurrentQuantity() + quantityToIncreaseInStoreStock);

        this.quantitiesItemsInBasketRepository.deleteByBasket_IdAndItem_ItemId(basketId, itemId);

        BasketEntity basketToUpdateOneItemLess = this.basketRepository.findBasketByIdEager(basketId).orElseThrow();
        List<ItemEntity> updatedItems = new ArrayList<>();
        List<ItemEntity> products = basketToUpdateOneItemLess.getProducts();
        for (ItemEntity product : products) {
            if (!Objects.equals(product.getItemId(), itemId)) {
                updatedItems.add(product);
            }
        }

        basketToUpdateOneItemLess.setProducts(updatedItems);
        this.basketRepository.save(basketToUpdateOneItemLess);

        return true;
    }

    public ItemQuantityInBasketEntity getItemFromItemQuantityInBasketEntityByBasketItem(
            BasketEntity basketOrder, ItemEntity basketOrderOneItem) {
        return this.quantitiesItemsInBasketRepository.findByBasket_IdAndItem_ItemId(
                basketOrder.getId(),
                basketOrderOneItem.getItemId());
    }

    public BasketEntity readOneBasket(Long basketId) {
        return this.basketRepository.findBasketByIdEager(basketId).orElseThrow();
    }

    public BasketEntity getOneBasket(Long basketId) {
        return this.basketRepository.findBasketOrderEntitiesById(basketId).orElseThrow();
    }

    public Long getBaskeIdByUserId(Long userId) {
        return this.basketRepository.findBasketIdByUserId(userId);
    }

    public void addBasketForRegisteredUser(UserEntity user) {
        BasketEntity basketOrder = new BasketEntity()
                .setUser(user)
                .setProducts(new ArrayList<>())
                .setBasketStatus(BasketStatus.OPEN)
                .setCreationDateTime(LocalDateTime.now());

        this.basketRepository.save(basketOrder);
    }

    @Transactional
    @Modifying
    public void resetOneBasketWhen20MinutesPassed(Long basketId) {
        List<ItemQuantityInBasketEntity> allQuantitiesForABasket = this.quantitiesItemsInBasketRepository.findAllByBasketId(basketId);
        for (ItemQuantityInBasketEntity item : allQuantitiesForABasket) {
            int quantityToReturn = item.getQuantityBought();
            int currentQuantity = item.getItem().getCurrentQuantity();
            ItemEntity itemEntity = item.getItem().setCurrentQuantity(currentQuantity + quantityToReturn);
            this.allItemsRepository.save(itemEntity);
        }

        this.quantitiesItemsInBasketRepository.deleteAllByBasket_Id(basketId);

        try {
            BasketEntity basketToReset = this.basketRepository.findBasketByIdEager(basketId).orElseThrow();
            basketToReset.setProducts(new ArrayList<ItemEntity>());
            basketToReset.setBasketStatus(BasketStatus.CLOSED);
            basketToReset.setCreationDateTime(null);
            this.basketRepository.save(basketToReset);
        } catch (Exception e) {
            LOGGER.warn("No present matching `baskets` entity so that we can set it to CLOSED status. No present matching respective `baskets_products` entity.");
        }
    }

    public List<BasketEntity> getAllBasketsCreatedMoreThan20MinutesAgo() {
        List<BasketEntity> resetListToReturn = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();

        List<BasketEntity> all = this.basketRepository.findAllBasketsLazyWithStatusOpen();
        for (BasketEntity basket : all) {
            LocalDateTime currentBasketCreationDateTime = basket.getCreationDateTime();
            LocalDateTime currentBasketWithAdded20Minutes = currentBasketCreationDateTime.plusMinutes(20L);
            if (currentBasketWithAdded20Minutes.isBefore(now)) {
                resetListToReturn.add(basket);
            }
        }

        return resetListToReturn;
    }

    public int getCountOfBaskets() {
        return this.basketRepository.findAll().size();
    }
}


