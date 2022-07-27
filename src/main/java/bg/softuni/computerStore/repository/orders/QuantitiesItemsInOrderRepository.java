package bg.softuni.computerStore.repository.orders;

import bg.softuni.computerStore.model.entity.orders.ItemQuantityInOrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface QuantitiesItemsInOrderRepository extends JpaRepository<ItemQuantityInOrderEntity, Long> {

    //iqo from ItemQuantityInOrderEntity
    @Query("SELECT iqo FROM ItemQuantityInOrderEntity iqo WHERE iqo.order.id= :uuid")
    List<ItemQuantityInOrderEntity> findAllByUUIDPrimary(UUID uuid);
}
