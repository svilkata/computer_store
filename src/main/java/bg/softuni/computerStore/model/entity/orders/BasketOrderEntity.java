package bg.softuni.computerStore.model.entity.orders;

import bg.softuni.computerStore.model.entity.BaseEntity;
import bg.softuni.computerStore.model.entity.products.ItemEntity;
import bg.softuni.computerStore.model.entity.users.UserEntity;
import bg.softuni.computerStore.model.enums.BasketStatus;

import javax.persistence.*;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "baskets")
public class BasketOrderEntity extends BaseEntity {
    @Enumerated(EnumType.STRING)
    @Column(name = "basket_status", nullable = false)
    private BasketStatus basketStatus;

    //(fetch = FetchType.EAGER)
    @ManyToMany
    private List<ItemEntity> products;

    //Keeping quantities of each bought product
//    private Map<String, Integer> productsQuantities = new LinkedHashMap<>();

    @ManyToOne
    private UserEntity user;

    public BasketOrderEntity() {
    }

    public UserEntity getUser() {
        return user;
    }

    public BasketOrderEntity setUser(UserEntity user) {
        this.user = user;
        return this;
    }

    public List<ItemEntity> getProducts() {
        return Collections.unmodifiableList(products); //in order not to transfer automatically the link of these products from Basket to FinalOrder
    }

    public BasketOrderEntity setProducts(List<ItemEntity> products) {
        this.products = products;
        return this;
    }

    public BasketStatus getBasketStatus() {
        return basketStatus;
    }

    public BasketOrderEntity setBasketStatus(BasketStatus basketStatus) {
        this.basketStatus = basketStatus;
        return this;
    }
}


