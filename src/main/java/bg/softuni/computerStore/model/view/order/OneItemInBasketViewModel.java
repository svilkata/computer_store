package bg.softuni.computerStore.model.view.order;

import java.math.BigDecimal;

public class OneItemInBasketViewModel {
    //From ItemEntity
    private Long itemId;
    private String type;
    private String model;
    private BigDecimal sellingPriceForAllQuantity;
    private String photoUrl;

    //For the current item of the basket
    private int quantity;

    public OneItemInBasketViewModel() {
    }

    public Long getItemId() {
        return itemId;
    }

    public OneItemInBasketViewModel setItemId(Long itemId) {
        this.itemId = itemId;
        return this;
    }

    public String getModel() {
        return model;
    }

    public OneItemInBasketViewModel setModel(String model) {
        this.model = model;
        return this;
    }

    public BigDecimal getSellingPriceForAllQuantity() {
        return sellingPriceForAllQuantity;
    }

    public OneItemInBasketViewModel setSellingPriceForAllQuantity(BigDecimal sellingPriceForAllQuantity) {
        this.sellingPriceForAllQuantity = sellingPriceForAllQuantity;
        return this;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public OneItemInBasketViewModel setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
        return this;
    }

    public int getQuantity() {
        return quantity;
    }

    public OneItemInBasketViewModel setQuantity(int quantity) {
        this.quantity = quantity;
        return this;
    }

    public String getType() {
        return type;
    }

    public OneItemInBasketViewModel setType(String type) {
        this.type = type;
        return this;
    }
}
