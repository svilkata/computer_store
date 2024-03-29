package bg.softuni.computerStore.repository.orders;

import bg.softuni.computerStore.model.entity.orders.FinalOrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface FinalOrderRepository extends JpaRepository<FinalOrderEntity, String> {
    Optional<FinalOrderEntity> findById(UUID uuid);

    Optional<FinalOrderEntity> findByOrderNumber(String orderNumber);

    @Query("SELECT o FROM FinalOrderEntity o JOIN FETCH o.products WHERE o.id= :uuid")
    Optional<FinalOrderEntity> findByOrderNumberByUUIDPrimaryEager(UUID uuid);

    @Query("SELECT distinct o FROM FinalOrderEntity o JOIN FETCH o.products")
    List<FinalOrderEntity> findAllOrderAndItemsEager();

    @Query("SELECT distinct o FROM FinalOrderEntity o")
    List<FinalOrderEntity> findAllOrdersLazy();

    List<FinalOrderEntity> findAllByUserId(Long userId);

    @Query("SELECT o FROM FinalOrderEntity o WHERE LOWER(o.orderNumber) LIKE CONCAT('%', :searchByOrderNumber, '%')")
    List<FinalOrderEntity> findAllOrdersLazyByOrderNumber(String searchByOrderNumber);

    @Query("SELECT o FROM FinalOrderEntity o WHERE o.user.id = :userId AND LOWER(o.orderNumber) LIKE CONCAT('%', :searchByOrderNumber, '%')")
    List<FinalOrderEntity> findAllOrdersLazyByUserIdAndOrderNumber(Long userId, String searchByOrderNumber);
}
