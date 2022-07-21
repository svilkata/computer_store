package bg.softuni.computerStore.repository.products;

import bg.softuni.computerStore.model.entity.products.ItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AllItemsRepository extends JpaRepository<ItemEntity, Long> {
    @Query("SELECT COUNT(i) FROM ItemEntity i WHERE i.type = :type")
    Long findCounItemsByType(@Param(value = "type") String type);


}
