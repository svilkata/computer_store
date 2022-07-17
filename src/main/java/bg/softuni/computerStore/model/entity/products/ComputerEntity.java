package bg.softuni.computerStore.model.entity.products;

import bg.softuni.computerStore.model.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;

@Entity
@Table(name = "computers")
public class ComputerEntity extends BaseEntity {
    @Column(nullable = false)
    private String name;
    @Column(name = "buying_price", nullable = false)
    private BigDecimal buyingPrice;
    @Column(name = "selling_price", nullable = false)
    private BigDecimal sellingPrice;
    @Column(nullable = false)
    private String processor;
    @Column(name = "video_card", nullable = false)
    private String videoCard;
    @Column(nullable = false)
    private String ram;
    private String disk;
    private String ssd;
    @Column(name = "more_info")
    private String moreInfo;


    public ComputerEntity() {
    }

    public String getName() {
        return name;
    }

    public ComputerEntity setName(String description) {
        this.name = description;
        return this;
    }

    public BigDecimal getBuyingPrice() {
        return buyingPrice;
    }

    public ComputerEntity setBuyingPrice(BigDecimal buyingPrice) {
        this.buyingPrice = buyingPrice;
        return this;
    }

    public BigDecimal getSellingPrice() {
        return sellingPrice;
    }

    public ComputerEntity setSellingPrice(BigDecimal sellingPrice) {
        this.sellingPrice = sellingPrice;
        return this;
    }

    public String getProcessor() {
        return processor;
    }

    public ComputerEntity setProcessor(String processor) {
        this.processor = processor;
        return this;
    }

    public String getVideoCard() {
        return videoCard;
    }

    public ComputerEntity setVideoCard(String videoCard) {
        this.videoCard = videoCard;
        return this;
    }

    public String getRam() {
        return ram;
    }

    public ComputerEntity setRam(String ram) {
        this.ram = ram;
        return this;
    }

    public String getDisk() {
        return disk;
    }

    public ComputerEntity setDisk(String disk) {
        this.disk = disk;
        return this;
    }

    public String getSsd() {
        return ssd;
    }

    public ComputerEntity setSsd(String ssd) {
        this.ssd = ssd;
        return this;
    }

    public String getMoreInfo() {
        return moreInfo;
    }

    public ComputerEntity setMoreInfo(String moreInfo) {
        this.moreInfo = moreInfo;
        return this;
    }
}
