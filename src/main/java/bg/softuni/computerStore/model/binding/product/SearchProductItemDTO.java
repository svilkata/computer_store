package bg.softuni.computerStore.model.binding.product;

public class SearchProductItemDTO {
    //in fact, the brand name is part of the model name
    private String model;

    private Integer minPrice;
    private Integer maxPrice;

    public SearchProductItemDTO() {
    }

    public String getModel() {
        return model;
    }

    public SearchProductItemDTO setModel(String model) {
        this.model = model;
        return this;
    }

    public Integer getMinPrice() {
        return minPrice;
    }

    public SearchProductItemDTO setMinPrice(Integer minPrice) {
        this.minPrice = minPrice;
        return this;
    }

    public Integer getMaxPrice() {
        return maxPrice;
    }

    public SearchProductItemDTO setMaxPrice(Integer maxPrice) {
        this.maxPrice = maxPrice;
        return this;
    }

    public boolean isEmpty(){
        return (model == null || model.isBlank()) && minPrice == null && maxPrice == null;
    }

    @Override
    public String toString() {
        return "SearchProductItemDTO{" +
                "model='" + model + '\'' +
                ", minPrice=" + minPrice +
                ", maxPrice=" + maxPrice +
                '}';
    }
}
