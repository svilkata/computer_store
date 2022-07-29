package bg.softuni.computerStore.model.entity.products;

import bg.softuni.computerStore.model.enums.TypesOfProducts;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.math.BigDecimal;
import java.util.Locale;

@Entity
@DiscriminatorValue(value = "laptop")
public class LaptopEntity extends ItemEntity{
    private static final String ITEM_TYPE = TypesOfProducts.LAPTOP.name().toLowerCase(Locale.ROOT);

    //TODO - Issue with SINGLE Table Inheritance strategy
    // how to adjust nullable = false and if null given somehow, some default value to be set for empty data
    @Column(nullable = true)
    private String resolution;

    public LaptopEntity() {
    }

    public LaptopEntity(String brand, String model, BigDecimal buyingPrice, BigDecimal sellingPrice, int newQuantity, String moreInfo, String resolution) {
        super(ITEM_TYPE, brand, model, buyingPrice, sellingPrice, newQuantity, moreInfo);
        this.resolution = resolution;
    }

    public String getResolution() {
        return resolution;
    }

    public LaptopEntity setResolution(String resolution) {
        this.resolution = resolution;
        return this;
    }
}
