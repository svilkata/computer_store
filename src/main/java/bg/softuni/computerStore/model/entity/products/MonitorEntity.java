package bg.softuni.computerStore.model.entity.products;

import bg.softuni.computerStore.model.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "monitors")
public class MonitorEntity extends ItemEntity {
    @Column(nullable = false)
    private String size;
    @Column(nullable = false)
    private String resolution;
    @Column(nullable = false, name = "matrix_type")
    private String matrixType;
    @Column(nullable = false, name = "view_angle")
    private String viewAngle;
    @Column(nullable = false, name = "refresh_rate")
    private String refreshRate;
    @Column(nullable = false)
    private String brightness;
    @Column(name = "more_info")
    private String moreInfo;

    public MonitorEntity() {
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
