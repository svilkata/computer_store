package bg.softuni.computerStore.model.view.product;

import java.math.BigDecimal;

public class LaptopViewGeneralModel {
    //From ItemEntity
    private Long itemId;
    private String brand;
    private String model;
    private BigDecimal sellingPrice;
    private Long currentQuantity;
    private String moreInfo;
    private String photoUrl;

    //From LaptopEntity
    private String resolution;

    public LaptopViewGeneralModel() {
    }

    public Long getItemId() {
        return itemId;
    }

    public LaptopViewGeneralModel setItemId(Long itemId) {
        this.itemId = itemId;
        return this;
    }

    public String getBrand() {
        return brand;
    }

    public LaptopViewGeneralModel setBrand(String brand) {
        this.brand = brand;
        return this;
    }

    public String getModel() {
        return model;
    }

    public LaptopViewGeneralModel setModel(String model) {
        this.model = model;
        return this;
    }

    public BigDecimal getSellingPrice() {
        return sellingPrice;
    }

    public LaptopViewGeneralModel setSellingPrice(BigDecimal sellingPrice) {
        this.sellingPrice = sellingPrice;
        return this;
    }

    public Long getCurrentQuantity() {
        return currentQuantity;
    }

    public LaptopViewGeneralModel setCurrentQuantity(Long currentQuantity) {
        this.currentQuantity = currentQuantity;
        return this;
    }

    public String getMoreInfo() {
        return moreInfo;
    }

    public LaptopViewGeneralModel setMoreInfo(String moreInfo) {
        this.moreInfo = moreInfo;
        return this;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public LaptopViewGeneralModel setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
        return this;
    }

    public String getResolution() {
        return resolution;
    }

    public LaptopViewGeneralModel setResolution(String resolution) {
        this.resolution = resolution;
        return this;
    }
}
