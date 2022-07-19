package bg.softuni.computerStore.model.entity.orders;

import bg.softuni.computerStore.model.entity.BaseEntity;
import bg.softuni.computerStore.model.entity.products.ComputerEntity;
import bg.softuni.computerStore.model.entity.products.ItemEntity;
import bg.softuni.computerStore.model.entity.users.UserEntity;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "baskets")
public class BasketOrderEntity extends BaseEntity {
//    @OneToMany(fetch = FetchType.EAGER)
    @ManyToMany
    private List<ItemEntity> products;


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
        return products;
    }

    public BasketOrderEntity setProducts(List<ItemEntity> products) {
        this.products = products;
        return this;
    }
}
