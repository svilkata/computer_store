package bg.softuni.computerStore.service;

import bg.softuni.computerStore.model.entity.orders.FinalOrderEntity;
import bg.softuni.computerStore.model.view.stats.StatsViewModelHttpRequests;
import bg.softuni.computerStore.model.view.stats.StatsViewModelReportSales;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class StatsService {
    private int anonymousRequests, authRequests;
    private final FinalOrderService finalOrderService;

    public StatsService(FinalOrderService finalOrderService) {
        this.finalOrderService = finalOrderService;
    }

    public void onRequest() {
        Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();

        if (authentication != null && (authentication.getPrincipal() instanceof UserDetails)) {
            authRequests++;
        } else {
            anonymousRequests++;
        }
    }

    public StatsViewModelHttpRequests getStatsHttpRequests() {
        return new StatsViewModelHttpRequests(authRequests, anonymousRequests);
    }

    public StatsViewModelReportSales getStatsSales() {
        List<FinalOrderEntity> allOrders = this.finalOrderService.getAllOrdersEager();

        BigDecimal[] totals = new BigDecimal[2];
        totals[0] = new BigDecimal(0);  //totalRevenue
        totals[1] = new BigDecimal(0);  //totalExpenditure
        int[] totalItems = new int[1];  //total number of items

        allOrders.forEach(ord -> {
            ord.getProducts().stream()
                    .forEach(produt -> {
                        totals[0] = totals[0].add(produt.getSellingPrice());
                        totals[1] = totals[1].add(produt.getBuyingPrice());
                        totalItems[0] += 1;
                    });
        });

        StatsViewModelReportSales sales = new StatsViewModelReportSales();
        sales
                .setTotalOrders(allOrders.size())
                .setTotalItems(totalItems[0])
                .setTotalRevenue(totals[0])
                .setTotalProfit(totals[0].subtract(totals[1]));

        return sales;
    }
}
