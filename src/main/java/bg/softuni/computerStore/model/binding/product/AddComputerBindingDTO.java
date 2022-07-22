package bg.softuni.computerStore.model.binding.product;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

public class AddComputerBindingDTO {
    @NotBlank(message = "Computer brand cannot be empty")
    private String brand;
    @NotBlank(message = "Computer model cannot be empty")
    private String model;
    @NotNull(message = "Computer new bought quantity can not be null")
    @Positive(message = "Computer new bought quantity should be positive")
    private int newQuantityToAdd;
    @NotNull(message = "Computer buying price can not be null")
    @Positive(message = "Computer buying price should be positive")
    private BigDecimal buyingPrice;
    @NotNull(message = "Computer selling price can not be null")
    @Positive(message = "Computer selling price should be positive")
    private BigDecimal sellingPrice;
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
    //this one can be empty, too :)
    private String moreInfo;

    public AddComputerBindingDTO() {
    }

    public BigDecimal getBuyingPrice() {
        return buyingPrice;
    }

    public AddComputerBindingDTO setBuyingPrice(BigDecimal buyingPrice) {
        this.buyingPrice = buyingPrice;
        return this;
    }

    public BigDecimal getSellingPrice() {
        return sellingPrice;
    }

    public AddComputerBindingDTO setSellingPrice(BigDecimal sellingPrice) {
        this.sellingPrice = sellingPrice;
        return this;
    }

    public int getNewQuantityToAdd() {
        return newQuantityToAdd;
    }

    public AddComputerBindingDTO setNewQuantityToAdd(int newQuantityToAdd) {
        this.newQuantityToAdd = newQuantityToAdd;
        return this;
    }

    public String getProcessor() {
        return processor;
    }

    public AddComputerBindingDTO setProcessor(String processor) {
        this.processor = processor;
        return this;
    }

    public String getVideoCard() {
        return videoCard;
    }

    public AddComputerBindingDTO setVideoCard(String videoCard) {
        this.videoCard = videoCard;
        return this;
    }

    public String getRam() {
        return ram;
    }

    public AddComputerBindingDTO setRam(String ram) {
        this.ram = ram;
        return this;
    }

    public String getDisk() {
        return disk;
    }

    public AddComputerBindingDTO setDisk(String disk) {
        this.disk = disk;
        return this;
    }

    public String getSsd() {
        return ssd;
    }

    public AddComputerBindingDTO setSsd(String ssd) {
        this.ssd = ssd;
        return this;
    }

    public String getMoreInfo() {
        return moreInfo;
    }

    public AddComputerBindingDTO setMoreInfo(String moreInfo) {
        this.moreInfo = moreInfo;
        return this;
    }

    public String getBrand() {
        return brand;
    }

    public AddComputerBindingDTO setBrand(String brand) {
        this.brand = brand;
        return this;
    }

    public String getModel() {
        return model;
    }

    public AddComputerBindingDTO setModel(String model) {
        this.model = model;
        return this;
    }
}
