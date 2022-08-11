package bg.softuni.computerStore.service.eventServices;

import bg.softuni.computerStore.repository.orders.FinalOrderRepository;
import org.springframework.stereotype.Service;

@Service
public class GlobalVariablesEventServices {
    private final FinalOrderRepository finalOrderRepository;

    public GlobalVariablesEventServices(FinalOrderRepository finalOrderRepository) {
        this.finalOrderRepository = finalOrderRepository;
        setStaticVariable();
    }

    public static int totalNumberOfOrders;

    private void setStaticVariable(){
        int ordersCreated = this.finalOrderRepository.findAll().size();
        totalNumberOfOrders = ordersCreated;
    }
}
