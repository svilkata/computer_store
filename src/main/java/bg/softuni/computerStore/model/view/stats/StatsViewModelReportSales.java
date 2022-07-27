package bg.softuni.computerStore.model.view.stats;

import java.math.BigDecimal;

public class StatsViewModelReportSales {
    private int totalOrders;
    private int totalItems;
    private BigDecimal totalRevenue;
    private BigDecimal totalProfit;

    public StatsViewModelReportSales() {
    }

    public int getTotalOrders() {
        return totalOrders;
    }

    public StatsViewModelReportSales setTotalOrders(int totalOrders) {
        this.totalOrders = totalOrders;
        return this;
    }

    public BigDecimal getTotalRevenue() {
        return totalRevenue;
    }

    public StatsViewModelReportSales setTotalRevenue(BigDecimal totalRevenue) {
        this.totalRevenue = totalRevenue;
        return this;
    }

    public BigDecimal getTotalProfit() {
        return totalProfit;
    }

    public StatsViewModelReportSales setTotalProfit(BigDecimal totalProfit) {
        this.totalProfit = totalProfit;
        return this;
    }

    public int getTotalItems() {
        return totalItems;
    }

    public StatsViewModelReportSales setTotalItems(int totalItems) {
        this.totalItems = totalItems;
        return this;
    }
}


