package bg.softuni.computerStore.repository.cloudinary;

import bg.softuni.computerStore.model.entity.cloudinary.PictureEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PictureRepository extends JpaRepository<PictureEntity, Long> {
    void deleteByPublicId(String publicId);

    @Query("SELECT ph.url FROM PictureEntity ph WHERE ph.itemId= :itemId")
    String findPhotoUrlByItemId(@Param(value = "itemId") Long itemId);

    Optional<PictureEntity> findPictureEntitiesByItemId(Long itemId);

    Optional<PictureEntity> findPictureEntityByPublicId(String photoPublicId);
}
