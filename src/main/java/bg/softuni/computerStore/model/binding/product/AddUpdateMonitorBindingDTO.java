package bg.softuni.computerStore.model.binding.product;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;

public class AddUpdateMonitorBindingDTO {
    //From ItemEntity
    //No validation for itemId
    private Long itemId;
    @NotBlank(message = "Computer brand cannot be empty")
    private String brand;
    @NotBlank(message = "Computer model cannot be empty")
    private String model;
    @NotNull(message = "Computer buying price can not be null")
    @Positive(message = "Computer buying price should be positive")
    private BigDecimal buyingPrice;
    @NotNull(message = "Computer selling price can not be null")
    @Positive(message = "Computer selling price should be positive")
    private BigDecimal sellingPrice;
    //No need for validation for current quantity
    private int currentQuantity;
    @NotNull(message = "Computer new bought quantity can not be null")
    @PositiveOrZero(message = "Computer new bought quantity should be positive or zero")
    private int newQuantityToAdd;
    //this one can be empty, too :)
    private String moreInfo;


    //From MonitorEntity
    @NotBlank(message = "Size cannot be empty")
    private String size;
    @NotBlank(message = "Resolution cannot be empty")
    private String resolution;
    @NotBlank(message = "Matrix type cannot be empty")
    private String matrixType;
    @NotBlank(message = "View angle cannot be empty")
    private String viewAngle;
    @NotBlank(message = "Refresh rate angle cannot be empty")
    private String refreshRate;
    //this one can be empty :)
    private String brightness;


    public AddUpdateMonitorBindingDTO() {
    }

    public Long getItemId() {
        return itemId;
    }

    public AddUpdateMonitorBindingDTO setItemId(Long itemId) {
        this.itemId = itemId;
        return this;
    }

    public String getBrand() {
        return brand;
    }

    public AddUpdateMonitorBindingDTO setBrand(String brand) {
        this.brand = brand;
        return this;
    }

    public String getModel() {
        return model;
    }

    public AddUpdateMonitorBindingDTO setModel(String model) {
        this.model = model;
        return this;
    }

    public BigDecimal getBuyingPrice() {
        return buyingPrice;
    }

    public AddUpdateMonitorBindingDTO setBuyingPrice(BigDecimal buyingPrice) {
        this.buyingPrice = buyingPrice;
        return this;
    }

    public BigDecimal getSellingPrice() {
        return sellingPrice;
    }

    public AddUpdateMonitorBindingDTO setSellingPrice(BigDecimal sellingPrice) {
        this.sellingPrice = sellingPrice;
        return this;
    }

    public int getCurrentQuantity() {
        return currentQuantity;
    }

    public AddUpdateMonitorBindingDTO setCurrentQuantity(int currentQuantity) {
        this.currentQuantity = currentQuantity;
        return this;
    }

    public int getNewQuantityToAdd() {
        return newQuantityToAdd;
    }

    public AddUpdateMonitorBindingDTO setNewQuantityToAdd(int newQuantityToAdd) {
        this.newQuantityToAdd = newQuantityToAdd;
        return this;
    }

    public String getMoreInfo() {
        return moreInfo;
    }

    public AddUpdateMonitorBindingDTO setMoreInfo(String moreInfo) {
        this.moreInfo = moreInfo;
        return this;
    }

    public String getSize() {
        return size;
    }

    public AddUpdateMonitorBindingDTO setSize(String size) {
        this.size = size;
        return this;
    }

    public String getResolution() {
        return resolution;
    }

    public AddUpdateMonitorBindingDTO setResolution(String resolution) {
        this.resolution = resolution;
        return this;
    }

    public String getMatrixType() {
        return matrixType;
    }

    public AddUpdateMonitorBindingDTO setMatrixType(String matrixType) {
        this.matrixType = matrixType;
        return this;
    }

    public String getViewAngle() {
        return viewAngle;
    }

    public AddUpdateMonitorBindingDTO setViewAngle(String viewAngle) {
        this.viewAngle = viewAngle;
        return this;
    }

    public String getRefreshRate() {
        return refreshRate;
    }

    public AddUpdateMonitorBindingDTO setRefreshRate(String refreshRate) {
        this.refreshRate = refreshRate;
        return this;
    }

    public String getBrightness() {
        return brightness;
    }

    public AddUpdateMonitorBindingDTO setBrightness(String brightness) {
        this.brightness = brightness;
        return this;
    }
}
