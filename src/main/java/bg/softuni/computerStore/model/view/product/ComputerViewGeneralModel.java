package bg.softuni.computerStore.model.view.product;

import java.math.BigDecimal;

public class ComputerViewGeneralModel {
    private Long itemId;
    private String brand;
    private String model;
    private BigDecimal sellingPrice;
    private String processor;
    private String videoCard;
    private String ram;
    private String disk;
    private String ssd;
    private String moreInfo;

    public ComputerViewGeneralModel() {
    }

    public Long getItemId() {
        return itemId;
    }

    public ComputerViewGeneralModel setItemId(Long itemId) {
        this.itemId = itemId;
        return this;
    }

    public String getBrand() {
        return brand;
    }

    public ComputerViewGeneralModel setBrand(String brand) {
        this.brand = brand;
        return this;
    }

    public String getModel() {
        return model;
    }

    public ComputerViewGeneralModel setModel(String model) {
        this.model = model;
        return this;
    }

    public BigDecimal getSellingPrice() {
        return sellingPrice;
    }

    public ComputerViewGeneralModel setSellingPrice(BigDecimal sellingPrice) {
        this.sellingPrice = sellingPrice;
        return this;
    }

    public String getProcessor() {
        return processor;
    }

    public ComputerViewGeneralModel setProcessor(String processor) {
        this.processor = processor;
        return this;
    }

    public String getVideoCard() {
        return videoCard;
    }

    public ComputerViewGeneralModel setVideoCard(String videoCard) {
        this.videoCard = videoCard;
        return this;
    }

    public String getRam() {
        return ram;
    }

    public ComputerViewGeneralModel setRam(String ram) {
        this.ram = ram;
        return this;
    }

    public String getDisk() {
        return disk;
    }

    public ComputerViewGeneralModel setDisk(String disk) {
        this.disk = disk;
        return this;
    }

    public String getSsd() {
        return ssd;
    }

    public ComputerViewGeneralModel setSsd(String ssd) {
        this.ssd = ssd;
        return this;
    }

    public String getMoreInfo() {
        return moreInfo;
    }

    public ComputerViewGeneralModel setMoreInfo(String moreInfo) {
        this.moreInfo = moreInfo;
        return this;
    }
}
