package bg.softuni.computerStore.model.entity.products;


import bg.softuni.computerStore.model.entity.picture.PictureEntity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "all_items")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type")
public abstract class ItemEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long itemId;

    //No setter for type!!!
    @Basic
    @Column(insertable = false, updatable = false)
    private String type;

    @Column(nullable = false)
    private String brand;
    //Our model should be unique!!!
    @Column(nullable = false, unique = true)
    private String model;
    @Column(name = "buying_price", nullable = false)
    private BigDecimal buyingPrice;
    @Column(name = "selling_price", nullable = false)
    private BigDecimal sellingPrice;
    @Column(name = "quantity")
    private int currentQuantity;
//    @Column(name = "more_info", columnDefinition = "TEXT")
    @Column(name = "more_info")
    private String moreInfo;
//    private String photoUrl;
    @OneToOne
    private PictureEntity photo;

    @Column(name = "creation_date_time", nullable = false)
    private LocalDateTime creationDateTime;

    public ItemEntity() {
    }

    public ItemEntity(String type, String brand, String model, BigDecimal buyingPrice, BigDecimal sellingPrice,
                      int newQuantity, String moreInfo) {
        this.type = type;
        this.brand = brand;
        this.model = model;
        this.buyingPrice = buyingPrice;
        this.sellingPrice = sellingPrice;
        this.currentQuantity = newQuantity;
        this.moreInfo = moreInfo;
    }

    public String getModel() {
        return model;
    }

    public ItemEntity setModel(String name) {
        this.model = name;
        return this;
    }

    public BigDecimal getBuyingPrice() {
        return buyingPrice;
    }

    public ItemEntity setBuyingPrice(BigDecimal buyingPrice) {
        this.buyingPrice = buyingPrice;
        return this;
    }

    public BigDecimal getSellingPrice() {
        return sellingPrice;
    }

    public ItemEntity setSellingPrice(BigDecimal sellingPrice) {
        this.sellingPrice = sellingPrice;
        return this;
    }

    public Long getItemId() {
        return itemId;
    }

    public ItemEntity setItemId(Long itemId) {
        this.itemId = itemId;
        return this;
    }

    public String getMoreInfo() {
        return moreInfo;
    }

    public ItemEntity setMoreInfo(String moreInfo) {
        this.moreInfo = moreInfo;
        return this;
    }

    public String getBrand() {
        return brand;
    }

    public ItemEntity setBrand(String brand) {
        this.brand = brand;
        return this;
    }

    public int getCurrentQuantity() {
        return currentQuantity;
    }

    public ItemEntity setCurrentQuantity(int updatedQuantity) {
        this.currentQuantity = updatedQuantity;
        return this;
    }

    @Override
    public String toString() {
        return "ItemEntity{" +
                "itemId=" + itemId +
                ", type='" + type + '\'' +
                ", brand='" + brand + '\'' +
                ", model='" + model + '\'' +
                ", buyingPrice=" + buyingPrice +
                ", sellingPrice=" + sellingPrice +
                ", updatedQuantity=" + currentQuantity +
                ", moreInfo='" + moreInfo + '\'' +
                '}';
    }

    public String getType() {
        return type;
    }

    public LocalDateTime getCreationDateTime() {
        return creationDateTime;
    }

    public ItemEntity setCreationDateTime(LocalDateTime creationDateTime) {
        this.creationDateTime = creationDateTime;
        return this;
    }

    public PictureEntity getPhoto() {
        return photo;
    }

    public ItemEntity setPhoto(PictureEntity photo) {
        this.photo = photo;
        return this;
    }
}
