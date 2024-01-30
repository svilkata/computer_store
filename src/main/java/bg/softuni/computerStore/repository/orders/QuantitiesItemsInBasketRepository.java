package bg.softuni.computerStore.repository.orders;

import bg.softuni.computerStore.model.entity.orders.ItemQuantityInBasketEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

//import org.springframework.data.jpa.repository.Query;

@Repository
public interface QuantitiesItemsInBasketRepository extends JpaRepository<ItemQuantityInBasketEntity, Long> {

    ItemQuantityInBasketEntity findByBasket_IdAndItem_ItemId(Long id, Long itemId);

//    @Query("DELETE FROM ItemQuantityInBasketEntity b WHERE b.basket.id = :basketId")
    void deleteAllByBasket_Id(Long basketId);

    void deleteByBasket_IdAndItem_ItemId(Long basketId, Long itemId);

    List<ItemQuantityInBasketEntity> findAllByBasketId(Long basketId);
}
