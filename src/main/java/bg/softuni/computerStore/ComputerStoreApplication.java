package bg.softuni.computerStore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ComputerStoreApplication {

    public static void main(String[] args) {
        SpringApplication.run(ComputerStoreApplication.class, args);
    }
}
