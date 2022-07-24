package bg.softuni.computerStore.model.entity.orders;

import bg.softuni.computerStore.model.entity.BaseEntity;
import bg.softuni.computerStore.model.entity.products.ItemEntity;
import bg.softuni.computerStore.model.entity.users.ClientExtraInfoEntity;
import bg.softuni.computerStore.model.entity.users.UserEntity;
import bg.softuni.computerStore.model.enums.OrderStatusEnum;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "orders")
public class FinalOrderEntity extends BaseEntity {
    private OrderStatusEnum status;
    private String customerOrderNumber;

    @ManyToMany
    private List<ItemEntity> products;

    @ManyToOne
    private UserEntity user;

    @OneToOne
    private ClientExtraInfoEntity extraInfoForCurrentOrder;

    public FinalOrderEntity() {
    }

    public OrderStatusEnum getStatus() {
        return status;
    }

    public FinalOrderEntity setStatus(OrderStatusEnum status) {
        this.status = status;
        return this;
    }

    public String getCustomerOrderNumber() {
        return customerOrderNumber;
    }

    public FinalOrderEntity setCustomerOrderNumber(String customerOrderNumber) {
        this.customerOrderNumber = customerOrderNumber;
        return this;
    }

    public List<ItemEntity> getProducts() {
        return products;
    }

    public FinalOrderEntity setProducts(List<ItemEntity> products) {
        this.products = products;
        return this;
    }

    public UserEntity getUser() {
        return user;
    }

    public FinalOrderEntity setUser(UserEntity user) {
        this.user = user;
        return this;
    }

    public ClientExtraInfoEntity getExtraInfoForCurrentOrder() {
        return extraInfoForCurrentOrder;
    }

    public FinalOrderEntity setExtraInfoForCurrentOrder(ClientExtraInfoEntity extraInfoForCurrentOrder) {
        this.extraInfoForCurrentOrder = extraInfoForCurrentOrder;
        return this;
    }
}
