package bg.softuni.computerStore.repository.orders;

import bg.softuni.computerStore.model.entity.orders.ItemQuantityInOrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuantitiesItemsInOrderRepository extends JpaRepository<ItemQuantityInOrderEntity, Long> {


}
