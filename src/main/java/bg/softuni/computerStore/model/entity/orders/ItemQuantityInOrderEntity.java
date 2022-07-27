package bg.softuni.computerStore.model.entity.orders;

import bg.softuni.computerStore.model.entity.BaseEntity;
import bg.softuni.computerStore.model.entity.products.ItemEntity;

import javax.persistence.*;

@Entity
@Table(name = "orders_item_quantity")
public class ItemQuantityInOrderEntity extends BaseEntity {
    @ManyToOne
    private FinalOrderEntity order;

    @ManyToOne
    private ItemEntity item;

    @Column(nullable = false, name = "bought_quantity")
    private int boughtQuantity;

    public ItemQuantityInOrderEntity() {
    }

    public FinalOrderEntity getOrder() {
        return order;
    }

    public ItemQuantityInOrderEntity setOrder(FinalOrderEntity order) {
        this.order = order;
        return this;
    }

    public ItemEntity getItem() {
        return item;
    }

    public ItemQuantityInOrderEntity setItem(ItemEntity item) {
        this.item = item;
        return this;
    }

    public int getBoughtQuantity() {
        return boughtQuantity;
    }

    public ItemQuantityInOrderEntity setBoughtQuantity(int boughtQuantity) {
        this.boughtQuantity = boughtQuantity;
        return this;
    }
}
