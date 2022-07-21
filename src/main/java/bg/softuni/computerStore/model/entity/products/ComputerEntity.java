package bg.softuni.computerStore.model.entity.products;

import bg.softuni.computerStore.model.entity.BaseEntity;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@DiscriminatorValue(value = "computer")
public class ComputerEntity extends ItemEntity {
    private static final String ITEM_TYPE = "computer";

    private String processor;
    @Column(name = "video_card")
    private String videoCard;
    private String ram;
    private String disk;
    private String ssd;
    @Column(name = "more_info")
    private String moreInfo;

    public ComputerEntity() {
    }

    public ComputerEntity(String name, BigDecimal buyingPrice, BigDecimal sellingPrice, int newQuantity) {
        super(ITEM_TYPE, name, buyingPrice, sellingPrice, newQuantity);
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

    @Override
    public String toString() {
        return super.toString() + "ComputerEntity{" +
                "processor='" + processor + '\'' +
                ", videoCard='" + videoCard + '\'' +
                ", ram='" + ram + '\'' +
                ", disk='" + disk + '\'' +
                ", ssd='" + ssd + '\'' +
                ", moreInfo='" + moreInfo + '\'' +
                '}';
    }
}
