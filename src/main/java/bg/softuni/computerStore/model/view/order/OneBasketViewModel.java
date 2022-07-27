package bg.softuni.computerStore.model.view.order;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

public class OneBasketViewModel {
    private BigDecimal totalValue;

    private List<OneItemInBasketViewModel> items;

    public OneBasketViewModel() {
    }

    public BigDecimal getTotalValue() {
        return totalValue;
    }

    public OneBasketViewModel setTotalValue(BigDecimal totalValue) {
        this.totalValue = totalValue;
        return this;
    }

    public List<OneItemInBasketViewModel> getItems() {
        return Collections.unmodifiableList(items);
    }

    public OneBasketViewModel setItems(List<OneItemInBasketViewModel> items) {
        this.items = items;
        return this;
    }
}
