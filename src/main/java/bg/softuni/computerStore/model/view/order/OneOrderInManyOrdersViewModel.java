package bg.softuni.computerStore.model.view.order;

import java.math.BigDecimal;

public class OneOrderInManyOrdersViewModel {
    private String username;
    private String orderNumber;
    private String createdAt;
    private int totalItemsInOrder;
    private BigDecimal totalValue;
    private String orderStatus;

    public OneOrderInManyOrdersViewModel() {
    }

    public String getUsername() {
        return username;
    }

    public OneOrderInManyOrdersViewModel setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public OneOrderInManyOrdersViewModel setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
        return this;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public OneOrderInManyOrdersViewModel setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public int getTotalItemsInOrder() {
        return totalItemsInOrder;
    }

    public OneOrderInManyOrdersViewModel setTotalItemsInOrder(int totalItemsInOrder) {
        this.totalItemsInOrder = totalItemsInOrder;
        return this;
    }

    public BigDecimal getTotalValue() {
        return totalValue;
    }

    public OneOrderInManyOrdersViewModel setTotalValue(BigDecimal totalValue) {
        this.totalValue = totalValue;
        return this;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public OneOrderInManyOrdersViewModel setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
        return this;
    }
}
