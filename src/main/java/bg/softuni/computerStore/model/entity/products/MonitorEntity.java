package bg.softuni.computerStore.model.entity.products;

import bg.softuni.computerStore.model.entity.BaseEntity;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@DiscriminatorValue(value = "monitor")
public class MonitorEntity extends ItemEntity {
    private static final String ITEM_TYPE = "monitor";

    private String size;
    private String resolution;
    @Column(name = "matrix_type")
    private String matrixType;
    @Column(name = "view_angle")
    private String viewAngle;
    @Column(name = "refresh_rate")
    private String refreshRate;
    private String brightness;
    @Column(name = "more_info")
    private String moreInfo;

    public MonitorEntity() {
    }

    public MonitorEntity(String name, BigDecimal buyingPrice, BigDecimal sellingPrice, int newQuantity) {
        super(ITEM_TYPE, name, buyingPrice, sellingPrice, newQuantity);
    }

    public String getSize() {
        return size;
    }

    public MonitorEntity setSize(String size) {
        this.size = size;
        return this;
    }

    public String getResolution() {
        return resolution;
    }

    public MonitorEntity setResolution(String resolution) {
        this.resolution = resolution;
        return this;
    }

    public String getMatrixType() {
        return matrixType;
    }

    public MonitorEntity setMatrixType(String matrixType) {
        this.matrixType = matrixType;
        return this;
    }

    public String getViewAngle() {
        return viewAngle;
    }

    public MonitorEntity setViewAngle(String viewAngle) {
        this.viewAngle = viewAngle;
        return this;
    }

    public String getRefreshRate() {
        return refreshRate;
    }

    public MonitorEntity setRefreshRate(String refreshRate) {
        this.refreshRate = refreshRate;
        return this;
    }

    public String getBrightness() {
        return brightness;
    }

    public MonitorEntity setBrightness(String brightness) {
        this.brightness = brightness;
        return this;
    }

    public String getMoreInfo() {
        return moreInfo;
    }

    public MonitorEntity setMoreInfo(String moreInfo) {
        this.moreInfo = moreInfo;
        return this;
    }

    @Override
    public String toString() {
        return super.toString() + "MonitorEntity{" +
                "size='" + size + '\'' +
                ", resolution='" + resolution + '\'' +
                ", matrixType='" + matrixType + '\'' +
                ", viewAngle='" + viewAngle + '\'' +
                ", refreshRate='" + refreshRate + '\'' +
                ", brightness='" + brightness + '\'' +
                ", moreInfo='" + moreInfo + '\'' +
                '}';
    }
}
