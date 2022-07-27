package bg.softuni.computerStore.repository.orders;

import bg.softuni.computerStore.model.entity.orders.ItemQuantityInBasketEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface QuantitiesItemsInBasketRepository extends JpaRepository<ItemQuantityInBasketEntity, Long> {

    ItemQuantityInBasketEntity findByBasket_IdAndItem_ItemId(Long id, Long itemId);

    Optional<ItemQuantityInBasketEntity> findByBasket_Id(Long id);

    void deleteAllByBasket_Id(Long id);

}
