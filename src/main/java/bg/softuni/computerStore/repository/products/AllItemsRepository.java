package bg.softuni.computerStore.repository.products;

import bg.softuni.computerStore.model.entity.products.ItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AllItemsRepository extends JpaRepository<ItemEntity, Long> {
    @Query("SELECT COUNT(i) FROM ItemEntity i WHERE i.type = :type")
    Long findCounItemsByType(@Param(value = "type") String type);


    @Query("SELECT c FROM ItemEntity c WHERE c.type = :type")
    List<ItemEntity> findAllComputersByType(String type);

    Optional<ItemEntity> findByModel(String model);
}
