package bg.softuni.computerStore;

import bg.softuni.computerStore.event.OrderCreatedEvent;
import bg.softuni.computerStore.service.FinalOrderService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationContextInitializedEvent;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ComputerStoreApplication {

	public static void main(String[] args) {
		SpringApplication.run(ComputerStoreApplication.class, args);
	}
}
