package bg.softuni.computerStore.model.entity.orders;

import bg.softuni.computerStore.model.entity.BaseEntity;
import bg.softuni.computerStore.model.entity.products.ItemEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "baskets_item_quantity")
public class ItemQuantityInBasketEntity extends BaseEntity {
    @ManyToOne
    private BasketEntity basket;

    @ManyToOne
    private ItemEntity item;

    @Column(name = "quantity_bought", nullable = false)
    private int quantityBought;

    public ItemQuantityInBasketEntity() {
    }

    public BasketEntity getBasket() {
        return basket;
    }

    public ItemQuantityInBasketEntity setBasket(BasketEntity basket) {
        this.basket = basket;
        return this;
    }

    public ItemEntity getItem() {
        return item;
    }

    public ItemQuantityInBasketEntity setItem(ItemEntity item) {
        this.item = item;
        return this;
    }

    public int getQuantityBought() {
        return quantityBought;
    }

    public ItemQuantityInBasketEntity setQuantityBought(int quantityBought) {
        this.quantityBought = quantityBought;
        return this;
    }
}
