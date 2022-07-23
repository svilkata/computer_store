package bg.softuni.computerStore.model.entity.cloudinary;

import javax.persistence.*;

@Entity
@Table(name = "pictures")
public class PictureEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long itemId;
    private String url;
    private String publicId;

    public PictureEntity() {
    }

    public Long getId() {
        return id;
    }

    public PictureEntity setId(Long id) {
        this.id = id;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public PictureEntity setUrl(String url) {
        this.url = url;
        return this;
    }

    public String getPublicId() {
        return publicId;
    }

    public PictureEntity setPublicId(String publicId) {
        this.publicId = publicId;
        return this;
    }

    public Long getItemId() {
        return itemId;
    }

    public PictureEntity setItemId(Long itemId) {
        this.itemId = itemId;
        return this;
    }
}
