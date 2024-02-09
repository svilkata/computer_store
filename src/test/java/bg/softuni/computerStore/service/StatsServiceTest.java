package bg.softuni.computerStore.service;

import bg.softuni.computerStore.service.picturesServices.PictureService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@AutoConfigureTestDatabase
class StatsServiceTest {
    @Autowired
    private PictureService pictureService;
    @Autowired
    private ComputerService computerService;
    @Autowired
    private MonitorService monitorService;
    @Autowired
    private LaptopService laptopService;

    @Autowired
    private UserService userService;

    @Autowired
    private BasketService basketService;
    @Autowired
    private FinalOrderService finalOrderService;

    @Autowired
    private StatsService statsService;

    @BeforeEach
    void setUp() {
        pictureService.init();
        computerService.init(); //7 items
        monitorService.init();  //3 items
        laptopService.init();  //1 item

        userService.init();

        this.basketService.init();
        this.finalOrderService.init();
    }

    @Test
    void getStatsSales() {
        this.statsService.getStatsSales();
    }
}