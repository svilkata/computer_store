package bg.softuni.computerStore.model.entity.orders;

import bg.softuni.computerStore.model.entity.BaseEntity;
import bg.softuni.computerStore.model.entity.products.ItemEntity;
import bg.softuni.computerStore.model.entity.users.UserEntity;
import bg.softuni.computerStore.model.enums.BasketStatus;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Entity
@Table(name = "baskets")
public class BasketEntity extends BaseEntity {
    @Enumerated(EnumType.STRING)
    @Column(name = "basket_status", nullable = false)
    private BasketStatus basketStatus;

    @Column(name = "creation_date_time")
    private LocalDateTime creationDateTime;

    @ManyToMany
    private List<ItemEntity> products;

    @ManyToOne
    private UserEntity user;

    public BasketEntity() {
    }

    public UserEntity getUser() {
        return user;
    }

    public BasketEntity setUser(UserEntity user) {
        this.user = user;
        return this;
    }

    public List<ItemEntity> getProducts() {
        return Collections.unmodifiableList(products); //in order not to transfer automatically the link of these products from Basket to FinalOrder
    }

    public BasketEntity setProducts(List<ItemEntity> products) {
        this.products = products;
        return this;
    }

    public BasketStatus getBasketStatus() {
        return basketStatus;
    }

    public BasketEntity setBasketStatus(BasketStatus basketStatus) {
        this.basketStatus = basketStatus;
        return this;
    }

    public LocalDateTime getCreationDateTime() {
        return creationDateTime;
    }

    public BasketEntity setCreationDateTime(LocalDateTime creationDateTime) {
        this.creationDateTime = creationDateTime;
        return this;
    }
}


