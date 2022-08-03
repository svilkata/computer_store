package bg.softuni.computerStore.model.view.order;

import java.math.BigDecimal;

public class OneItemInOrderViewModel {
    //From ItemEntity
    private Long itemId;
    private String type;
    private String model;
    private BigDecimal sellingPriceForAllQuantity;
    private BigDecimal pricePerUnit;
    private String photoUrl;

    //For the current item of the basket
    private int quantity;

    public OneItemInOrderViewModel() {
    }

    public Long getItemId() {
        return itemId;
    }

    public OneItemInOrderViewModel setItemId(Long itemId) {
        this.itemId = itemId;
        return this;
    }

    public String getModel() {
        return model;
    }

    public OneItemInOrderViewModel setModel(String model) {
        this.model = model;
        return this;
    }

    public BigDecimal getSellingPriceForAllQuantity() {
        return sellingPriceForAllQuantity;
    }

    public OneItemInOrderViewModel setSellingPriceForAllQuantity(BigDecimal sellingPriceForAllQuantity) {
        this.sellingPriceForAllQuantity = sellingPriceForAllQuantity;
        return this;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public OneItemInOrderViewModel setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
        return this;
    }

    public int getQuantity() {
        return quantity;
    }

    public OneItemInOrderViewModel setQuantity(int quantity) {
        this.quantity = quantity;
        return this;
    }

    public String getType() {
        return type;
    }

    public OneItemInOrderViewModel setType(String type) {
        this.type = type;
        return this;
    }

    public BigDecimal getPricePerUnit() {
        return pricePerUnit;
    }

    public OneItemInOrderViewModel setPricePerUnit(BigDecimal pricePerUnit) {
        this.pricePerUnit = pricePerUnit;
        return this;
    }
}
