package bg.softuni.computer_store.repository;

import bg.softuni.computer_store.model.entity.UserEntity;
import bg.softuni.computer_store.service.AppUserDetailsService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByUsername(String username);
}
