package bg.softuni.computerStore.repository.orders;

import bg.softuni.computerStore.model.entity.orders.BasketEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BasketRepository extends JpaRepository<BasketEntity, Long> {
    @Query("SELECT b FROM BasketEntity b JOIN FETCH b.products WHERE b.id= :id")
    Optional<BasketEntity> findBasketByIdEager(Long id);

    Optional<BasketEntity> findBasketOrderEntitiesById(Long id);

    @Query("SELECT b FROM BasketEntity b WHERE b.id= :id")
    Optional<BasketEntity> findBasketByIdLazy(Long id);

    @Query("SELECT b.id FROM BasketEntity b WHERE b.user.id= :userId")
    Long findBasketIdByUserId(Long userId);

    @Query("SELECT b.user.id FROM BasketEntity b WHERE b.id= :basketId")
    Long findUserIdByBasketId(Long basketId);

    @Query("SELECT b FROM BasketEntity b WHERE b.basketStatus = 'OPEN'")
    List<BasketEntity> findAllBasketsLazyWithStatusOpen();
}
