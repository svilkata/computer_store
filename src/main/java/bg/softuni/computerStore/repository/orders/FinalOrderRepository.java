package bg.softuni.computerStore.repository.orders;

import bg.softuni.computerStore.model.entity.orders.FinalOrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FinalOrderRepository extends JpaRepository<FinalOrderEntity, String> {
}
