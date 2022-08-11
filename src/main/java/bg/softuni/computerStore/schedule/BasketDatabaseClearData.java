package bg.softuni.computerStore.schedule;

import bg.softuni.computerStore.model.entity.orders.BasketOrderEntity;
import bg.softuni.computerStore.service.BasketService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class BasketDatabaseClearData {
    private static final Logger LOGGER = LoggerFactory.getLogger(BasketDatabaseClearData.class);

    private final BasketService basketService;

    public BasketDatabaseClearData(BasketService basketService) {
        this.basketService = basketService;
    }

    //<second><minute><hour><day-of-month><month><day-of-week>
    //on each 15 minutes
    @Scheduled(fixedRate  = 300000, initialDelay = 60000)
    public void resetBaskets(){
        LOGGER.info("Baskets reseting/clearing operation started at {}", LocalDateTime.now());
        List<BasketOrderEntity> basketsCreatedMoreThan20MinutesAgo = this.basketService.getAllBasketsCreatedMoreThan20MinutesAgo();

        for (BasketOrderEntity basket : basketsCreatedMoreThan20MinutesAgo) {
            this.basketService.resetOneBasketWhen20MinutesPassed(basket.getId());
        }


        LOGGER.info("{} baskets successfully cleared at {}", basketsCreatedMoreThan20MinutesAgo.size(), LocalDateTime.now());
    }
}
