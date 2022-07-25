package bg.softuni.computerStore.model.view.product;

import javax.persistence.Column;
import java.math.BigDecimal;

public class MonitorViewGeneralModel {
    //From ItemEntity
    private Long itemId;
    private String brand;
    private String model;
    private BigDecimal sellingPrice;
    private Long currentQuantity;
    private String moreInfo;
    private String photoUrl;

    //From MonitorEntity
    private String size;
    private String resolution;
    private String matrixType;
    private String viewAngle;
    private String refreshRate;
    private String brightness;

    public MonitorViewGeneralModel() {
    }

    public Long getItemId() {
        return itemId;
    }

    public MonitorViewGeneralModel setItemId(Long itemId) {
        this.itemId = itemId;
        return this;
    }

    public String getBrand() {
        return brand;
    }

    public MonitorViewGeneralModel setBrand(String brand) {
        this.brand = brand;
        return this;
    }

    public String getModel() {
        return model;
    }

    public MonitorViewGeneralModel setModel(String model) {
        this.model = model;
        return this;
    }

    public BigDecimal getSellingPrice() {
        return sellingPrice;
    }

    public MonitorViewGeneralModel setSellingPrice(BigDecimal sellingPrice) {
        this.sellingPrice = sellingPrice;
        return this;
    }

    public String getMoreInfo() {
        return moreInfo;
    }

    public MonitorViewGeneralModel setMoreInfo(String moreInfo) {
        this.moreInfo = moreInfo;
        return this;
    }

    public Long getCurrentQuantity() {
        return currentQuantity;
    }

    public MonitorViewGeneralModel setCurrentQuantity(Long currentQuantity) {
        this.currentQuantity = currentQuantity;
        return this;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public MonitorViewGeneralModel setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
        return this;
    }

    public String getSize() {
        return size;
    }

    public MonitorViewGeneralModel setSize(String size) {
        this.size = size;
        return this;
    }

    public String getResolution() {
        return resolution;
    }

    public MonitorViewGeneralModel setResolution(String resolution) {
        this.resolution = resolution;
        return this;
    }

    public String getMatrixType() {
        return matrixType;
    }

    public MonitorViewGeneralModel setMatrixType(String matrixType) {
        this.matrixType = matrixType;
        return this;
    }

    public String getViewAngle() {
        return viewAngle;
    }

    public MonitorViewGeneralModel setViewAngle(String viewAngle) {
        this.viewAngle = viewAngle;
        return this;
    }

    public String getRefreshRate() {
        return refreshRate;
    }

    public MonitorViewGeneralModel setRefreshRate(String refreshRate) {
        this.refreshRate = refreshRate;
        return this;
    }

    public String getBrightness() {
        return brightness;
    }

    public MonitorViewGeneralModel setBrightness(String brightness) {
        this.brightness = brightness;
        return this;
    }
}
