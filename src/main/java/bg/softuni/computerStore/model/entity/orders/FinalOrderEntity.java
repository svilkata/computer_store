package bg.softuni.computerStore.model.entity.orders;

import bg.softuni.computerStore.model.entity.products.ItemEntity;
import bg.softuni.computerStore.model.entity.users.UserEntity;
import bg.softuni.computerStore.model.enums.OrderStatusEnum;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "orders")
public class FinalOrderEntity {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    //    @Type(type = "uuid-binary")
    @Type(type = "uuid-char")
    private UUID id;

    @Column(name = "order_number")
    private String orderNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatusEnum status;

    @Column(name = "creation_date_time", nullable = false)
    private LocalDateTime creationDateTime;

    @Column(nullable = false)
    @Positive
    private BigDecimal totalTotal;

    @Column(name = "count_total_products")
    private int countTotalProducts;

    @ManyToMany
    private List<ItemEntity> products;

    @ManyToOne
    private UserEntity user;

    @OneToOne
    private ClientOrderExtraInfoEntity extraInfoForCurrentOrder;

    public FinalOrderEntity() {
    }

    public OrderStatusEnum getStatus() {
        return status;
    }

    public FinalOrderEntity setStatus(OrderStatusEnum status) {
        this.status = status;
        return this;
    }

    public UserEntity getUser() {
        return user;
    }

    public FinalOrderEntity setUser(UserEntity user) {
        this.user = user;
        return this;
    }

    public ClientOrderExtraInfoEntity getExtraInfoForCurrentOrder() {
        return extraInfoForCurrentOrder;
    }

    public FinalOrderEntity setExtraInfoForCurrentOrder(ClientOrderExtraInfoEntity extraInfoForCurrentOrder) {
        this.extraInfoForCurrentOrder = extraInfoForCurrentOrder;
        return this;
    }

    public UUID getId() {
        return id;
    }

    public FinalOrderEntity setId(UUID id) {
        this.id = id;
        return this;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public FinalOrderEntity setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
        return this;
    }

    public List<ItemEntity> getProducts() {
        return Collections.unmodifiableList(products);
    }

    public FinalOrderEntity setProducts(List<ItemEntity> products) {
        this.products = products;
        return this;
    }

    public LocalDateTime getCreationDateTime() {
        return creationDateTime;
    }

    public FinalOrderEntity setCreationDateTime(LocalDateTime dateTime) {
        this.creationDateTime = dateTime;
        return this;
    }

    public BigDecimal getTotalTotal() {
        return totalTotal;
    }

    public FinalOrderEntity setTotalTotal(BigDecimal totalTotal) {
        this.totalTotal = totalTotal;
        return this;
    }

    public int getCountTotalProducts() {
        return countTotalProducts;
    }

    public FinalOrderEntity setCountTotalProducts(int countTotalProducts) {
        this.countTotalProducts = countTotalProducts;
        return this;
    }
}
