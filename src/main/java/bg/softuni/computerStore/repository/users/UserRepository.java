package bg.softuni.computerStore.repository.users;

import bg.softuni.computerStore.model.entity.users.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByUsername(String username);

    Optional<UserEntity> findByEmail(String email);

    //A user with user roles size more than 1 is not a customer :)
    @Query("FROM UserEntity u" +
            " WHERE size(u.userRoles) > 1")
    List<UserEntity> findAllEmployeeUsers();

    @Query("SELECT distinct u FROM UserEntity u" +
            " JOIN u.userRoles AS r" +
            " WHERE r.userRole='ADMIN'")
    UserEntity getCurrentAdminUser();
}
