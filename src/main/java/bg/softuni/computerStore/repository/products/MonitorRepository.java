package bg.softuni.computerStore.repository.products;

import bg.softuni.computerStore.model.entity.products.MonitorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MonitorRepository extends JpaRepository<MonitorEntity, Long> {
}
