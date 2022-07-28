package bg.softuni.computerStore.model.binding.product;

import bg.softuni.computerStore.model.validation.price.ValidPrice;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;

public class AddUpdateComputerBindingDTO {
    //From ItemEntity
    //No validation for itemId
    private Long itemId;
    @NotBlank(message = "Computer brand cannot be empty")
    private String brand;
    @NotBlank(message = "Computer model cannot be empty")
    private String model;
    @ValidPrice
    private String buyingPrice;
    @ValidPrice
    private String sellingPrice;
    //No need for validation for current quantity
    private int currentQuantity;
    @NotNull(message = "Computer new bought quantity can not be null")
    @PositiveOrZero(message = "Computer new bought quantity should be positive or zero")
    private int newQuantityToAdd;
    //this one can be empty, too :)
    private String moreInfo;


    //From ComputerEntity
    @NotBlank(message = "Processor cannot be empty")
    private String processor;
    @NotBlank(message = "Video card cannot be empty")
    private String videoCard;
    @NotBlank(message = "Ram cannot be empty")
    private String ram;
    @NotBlank(message = "Disk cannot be empty")
    private String disk;
    //this one can be empty :)
    private String ssd;


    public AddUpdateComputerBindingDTO() {
    }

    public String getBuyingPrice() {
        return buyingPrice;
    }

    public AddUpdateComputerBindingDTO setBuyingPrice(String buyingPrice) {
        this.buyingPrice = buyingPrice;
        return this;
    }

    public String getSellingPrice() {
        return sellingPrice;
    }

    public AddUpdateComputerBindingDTO setSellingPrice(String sellingPrice) {
        this.sellingPrice = sellingPrice;
        return this;
    }

    public int getNewQuantityToAdd() {
        return newQuantityToAdd;
    }

    public AddUpdateComputerBindingDTO setNewQuantityToAdd(int newQuantityToAdd) {
        this.newQuantityToAdd = newQuantityToAdd;
        return this;
    }

    public String getProcessor() {
        return processor;
    }

    public AddUpdateComputerBindingDTO setProcessor(String processor) {
        this.processor = processor;
        return this;
    }

    public String getVideoCard() {
        return videoCard;
    }

    public AddUpdateComputerBindingDTO setVideoCard(String videoCard) {
        this.videoCard = videoCard;
        return this;
    }

    public String getRam() {
        return ram;
    }

    public AddUpdateComputerBindingDTO setRam(String ram) {
        this.ram = ram;
        return this;
    }

    public String getDisk() {
        return disk;
    }

    public AddUpdateComputerBindingDTO setDisk(String disk) {
        this.disk = disk;
        return this;
    }

    public String getSsd() {
        return ssd;
    }

    public AddUpdateComputerBindingDTO setSsd(String ssd) {
        this.ssd = ssd;
        return this;
    }

    public String getMoreInfo() {
        return moreInfo;
    }

    public AddUpdateComputerBindingDTO setMoreInfo(String moreInfo) {
        this.moreInfo = moreInfo;
        return this;
    }

    public String getBrand() {
        return brand;
    }

    public AddUpdateComputerBindingDTO setBrand(String brand) {
        this.brand = brand;
        return this;
    }

    public String getModel() {
        return model;
    }

    public AddUpdateComputerBindingDTO setModel(String model) {
        this.model = model;
        return this;
    }

    public Long getItemId() {
        return itemId;
    }

    public AddUpdateComputerBindingDTO setItemId(Long itemId) {
        this.itemId = itemId;
        return this;
    }

    public int getCurrentQuantity() {
        return currentQuantity;
    }

    public AddUpdateComputerBindingDTO setCurrentQuantity(int currentQuantity) {
        this.currentQuantity = currentQuantity;
        return this;
    }
}
