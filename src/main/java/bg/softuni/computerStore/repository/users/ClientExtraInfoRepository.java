package bg.softuni.computerStore.repository.users;


import bg.softuni.computerStore.model.entity.users.ClientExtraInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientExtraInfoRepository extends JpaRepository<ClientExtraInfoEntity, Long> {
}
