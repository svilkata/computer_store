package bg.softuni.computerStore.model.entity.products;

import bg.softuni.computerStore.model.enums.TypesOfProducts;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.math.BigDecimal;
import java.util.Locale;

@Entity
@DiscriminatorValue(value = "computer")
public class ComputerEntity extends ItemEntity {
    private static final String ITEM_TYPE = TypesOfProducts.COMPUTER.toString().toLowerCase(Locale.ROOT);

    //TODO - Issue with SINGLE Table Inheritance strategy
    // how to adjust nullable = false and if null given somehow, some default value to be set for empty data
    @Column(nullable = true)
    private String processor;
    @Column(name = "video_card", nullable = true)
    private String videoCard;
    @Column(nullable = true)
    private String ram;
    @Column(nullable = true)
    private String disk;
    //not mandatory
    private String ssd;

    public ComputerEntity() {
    }

    public ComputerEntity(String brand, String model, BigDecimal buyingPrice, BigDecimal sellingPrice, int newQuantity, String moreInfo) {
        super(ITEM_TYPE, brand, model, buyingPrice, sellingPrice, newQuantity, moreInfo);
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


    @Override
    public String toString() {
        return super.toString() + "ComputerEntity{" +
                "processor='" + processor + '\'' +
                ", videoCard='" + videoCard + '\'' +
                ", ram='" + ram + '\'' +
                ", disk='" + disk + '\'' +
                ", ssd='" + ssd + '\'' +
                '}';
    }
}
