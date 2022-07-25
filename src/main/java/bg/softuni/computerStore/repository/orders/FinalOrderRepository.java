package bg.softuni.computerStore.repository.orders;

import bg.softuni.computerStore.model.entity.orders.FinalOrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface FinalOrderRepository extends JpaRepository<FinalOrderEntity, String> {
    Optional<FinalOrderEntity> findById(UUID uuid);

    Optional<FinalOrderEntity> findByOrderNumber(String orderNumber);

    @Query("SELECT o FROM FinalOrderEntity o JOIN FETCH o.products WHERE o.id= :uuid")
    Optional<FinalOrderEntity> findByOrderNumberEager(UUID uuid);
}
