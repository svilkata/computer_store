package bg.softuni.computerStore.model.binding.product;

import javax.validation.constraints.NotBlank;

public class ProductItemTypeBindingDTO {
    @NotBlank
    private String type;

    @NotBlank
    private String model;

    private Long itemId;

    public ProductItemTypeBindingDTO() {
    }

    public String getType() {
        return type;
    }

    public ProductItemTypeBindingDTO setType(String type) {
        this.type = type;
        return this;
    }

    public String getModel() {
        return model;
    }

    public ProductItemTypeBindingDTO setModel(String model) {
        this.model = model;
        return this;
    }

    public Long getItemId() {
        return itemId;
    }

    public ProductItemTypeBindingDTO setItemId(Long itemId) {
        this.itemId = itemId;
        return this;
    }
}
