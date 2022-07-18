package bg.softuni.computerStore.repository.products;

import bg.softuni.computerStore.model.entity.products.ComputerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ComputerRepository extends JpaRepository<ComputerEntity, Long> {
}
