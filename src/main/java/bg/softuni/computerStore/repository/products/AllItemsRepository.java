package bg.softuni.computerStore.repository.products;

import bg.softuni.computerStore.config.mapper.StructMapper;
import bg.softuni.computerStore.model.binding.product.SearchProductItemDTO;
import bg.softuni.computerStore.model.entity.products.ItemEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AllItemsRepository
        extends PagingAndSortingRepository<ItemEntity, Long>, JpaSpecificationExecutor<ItemEntity>
{
    @Query("SELECT COUNT(i) FROM ItemEntity i WHERE i.type = :type")
    Long findCounItemsByType(@Param(value = "type") String type);


    @Query("SELECT c FROM ItemEntity c WHERE c.type = :type")
    List<ItemEntity> findAllItemsByType(String type);

    Optional<ItemEntity> findByModel(String model);

    Optional<ItemEntity> findItemEntityByTypeAndItemId(String type, Long itemId);

    Page<ItemEntity> findAllByType(String type, Pageable pageable);

//    Page<ItemEntity> findAllByTypeAndSearch(String type, Pageable pageable, SearchProductItemDTO searchProductItemDTO);
}
