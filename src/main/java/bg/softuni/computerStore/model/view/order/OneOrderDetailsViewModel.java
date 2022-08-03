package bg.softuni.computerStore.model.view.order;

import java.math.BigDecimal;
import java.util.List;

public class OneOrderDetailsViewModel {
    private String orderNumber;
    private BigDecimal totalTotal;
    private String deliveryAddress;
    private String phoneNumber;
    private String extraInfo;

    private List<OneItemInOrderViewModel> items;

    public OneOrderDetailsViewModel() {
    }

    public BigDecimal getTotalTotal() {
        return totalTotal;
    }

    public OneOrderDetailsViewModel setTotalTotal(BigDecimal totalTotal) {
        this.totalTotal = totalTotal;
        return this;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public OneOrderDetailsViewModel setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
        return this;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public OneOrderDetailsViewModel setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public String getExtraInfo() {
        return extraInfo;
    }

    public OneOrderDetailsViewModel setExtraInfo(String extraInfo) {
        this.extraInfo = extraInfo;
        return this;
    }

    public List<OneItemInOrderViewModel> getItems() {
        return items;
    }

    public OneOrderDetailsViewModel setItems(List<OneItemInOrderViewModel> items) {
        this.items = items;
        return this;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public OneOrderDetailsViewModel setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
        return this;
    }
}
