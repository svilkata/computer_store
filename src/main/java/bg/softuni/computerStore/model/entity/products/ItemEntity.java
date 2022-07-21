package bg.softuni.computerStore.model.entity.products;


import bg.softuni.computerStore.model.entity.orders.BasketOrderEntity;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "all_items")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type")
public abstract class ItemEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long itemId;

    @Basic
    @Column(insertable = false, updatable = false)
    private String type;

    @Column(nullable = false)
    private String name;
    @Column(name = "buying_price", nullable = false)
    private BigDecimal buyingPrice;
    @Column(name = "selling_price", nullable = false)
    private BigDecimal sellingPrice;
    @Column(name = "quantity")
    private int updatedQuantity;

    public ItemEntity() {
    }

    public ItemEntity(String type, String name, BigDecimal buyingPrice, BigDecimal sellingPrice, int newQuantity) {
        this.type = type;
        this.name = name;
        this.buyingPrice = buyingPrice;
        this.sellingPrice = sellingPrice;
        this.updatedQuantity = newQuantity;
    }

    public String getName() {
        return name;
    }

    public ItemEntity setName(String name) {
        this.name = name;
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

    public String getType() {
        return type;
    }

    public ItemEntity setType(String type) {
        this.type = type;
        return this;
    }


    @Override
    public String toString() {
        return "ItemEntity{" +
                "itemId=" + itemId +
                ", type='" + type + '\'' +
                ", name='" + name + '\'' +
                ", buyingPrice=" + buyingPrice +
                ", sellingPrice=" + sellingPrice +
                '}';
    }
}
