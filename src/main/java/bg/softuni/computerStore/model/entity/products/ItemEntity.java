package bg.softuni.computerStore.model.entity.products;

import bg.softuni.computerStore.model.entity.orders.BasketOrderEntity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "all_items")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class ItemEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long itemId;

    @Column(nullable = false)
    private String name;
    @Column(name = "buying_price", nullable = false)
    private BigDecimal buyingPrice;
    @Column(name = "selling_price", nullable = false)
    private BigDecimal sellingPrice;

    @ManyToOne
    private BasketOrderEntity basket;

    public ItemEntity() {
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


    @Override
    public String toString() {
        return "ItemEntity{" +
                "itemId=" + itemId +
                ", name='" + name + '\'' +
                ", buyingPrice=" + buyingPrice +
                ", sellingPrice=" + sellingPrice +
                ", basket=" + basket +
                '}';
    }
}
