package bg.softuni.computerStore.service.eventServices;

import bg.softuni.computerStore.event.OrderCreatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
public class CountOrdersService {
    private static final Logger LOGGER = LoggerFactory.getLogger(BonusPointsService.class);

    @EventListener(OrderCreatedEvent.class)
    public void onOrderCreated(OrderCreatedEvent oce) {
        GlobalVariablesEventServices.totalNumberOfOrders++;

        LOGGER.info("Count of orders increased with 1 - newly created order with id: {}", oce.getOrderId());

    }
}
