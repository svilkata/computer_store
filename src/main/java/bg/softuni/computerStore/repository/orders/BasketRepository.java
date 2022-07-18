package bg.softuni.computerStore.repository.orders;

import bg.softuni.computerStore.model.entity.orders.BasketOrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BasketRepository extends JpaRepository<BasketOrderEntity, Long> {
}
