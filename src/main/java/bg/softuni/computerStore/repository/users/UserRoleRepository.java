package bg.softuni.computerStore.repository.users;

import bg.softuni.computerStore.model.entity.users.UserRoleEntity;
import bg.softuni.computerStore.model.enums.UserRoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRoleEntity, Long> {
    Optional<UserRoleEntity> findByUserRole(UserRoleEnum roleEnum);
}
