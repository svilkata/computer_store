package bg.softuni.computerStore.service.eventServices;

import bg.softuni.computerStore.event.OrderCreatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
public class BonusPointsService {
    private static final Logger LOGGER = LoggerFactory.getLogger(BonusPointsService.class);

    @EventListener(OrderCreatedEvent.class)
    public void onOrderCreated(OrderCreatedEvent oce) {
        LOGGER.info("Adding bonus points to user for creating order {}", oce.getOrderId());
    }
}