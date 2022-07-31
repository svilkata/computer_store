package bg.softuni.computerStore.repository.orders;

import bg.softuni.computerStore.model.entity.orders.BasketOrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BasketRepository extends JpaRepository<BasketOrderEntity, Long> {
    @Query("SELECT b FROM BasketOrderEntity b JOIN FETCH b.products WHERE b.id= :id")
    Optional<BasketOrderEntity> findBasketByIdEager(Long id);

    Optional<BasketOrderEntity> findBasketOrderEntitiesById(Long id);

    @Query("SELECT b FROM BasketOrderEntity b WHERE b.id= :id")
    Optional<BasketOrderEntity> findBasketByIdLazy(Long id);

    @Query("SELECT b.id FROM BasketOrderEntity b WHERE b.user.id= :userId")
    Long findBasketIdByUserId(Long userId);

    @Query("SELECT b.user.id FROM BasketOrderEntity b WHERE b.id= :basketId")
    Long findUserIdByBasketId(Long basketId);
}
