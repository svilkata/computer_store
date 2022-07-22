package bg.softuni.computerStore.model.binding.product;

import javax.validation.constraints.NotBlank;

public class ProductItemTypeBindingDTO {
    @NotBlank
    private String type;

    public ProductItemTypeBindingDTO() {
    }

    public String getType() {
        return type;
    }

    public ProductItemTypeBindingDTO setType(String type) {
        this.type = type;
        return this;
    }
}
