package bg.softuni.computerStore.model.view.order;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

public class OneOrderViewModel {
    private BigDecimal totalValue;

    private List<OneItemInOrderViewModel> items;

    public OneOrderViewModel() {
    }

    public BigDecimal getTotalValue() {
        return totalValue;
    }

    public OneOrderViewModel setTotalValue(BigDecimal totalValue) {
        this.totalValue = totalValue;
        return this;
    }

    public List<OneItemInOrderViewModel> getItems() {
        return Collections.unmodifiableList(items);
    }

    public OneOrderViewModel setItems(List<OneItemInOrderViewModel> items) {
        this.items = items;
        return this;
    }
}
