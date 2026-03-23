package com.inventory.dto;
import java.math.BigDecimal;
public class DashboardStats {
    public long totalProducts, lowStockCount, pendingRequests, totalTransactions;
    public BigDecimal totalSalesThisMonth, totalPurchasesThisMonth;
    public DashboardStats(){}
    public DashboardStats(long tp, long ls, long pr, BigDecimal sales, BigDecimal purchases, long tx){
        totalProducts=tp; lowStockCount=ls; pendingRequests=pr;
        totalSalesThisMonth=sales; totalPurchasesThisMonth=purchases; totalTransactions=tx;
    }
    public long getTotalProducts(){ return totalProducts; } public void setTotalProducts(long v){ totalProducts=v; }
    public long getLowStockCount(){ return lowStockCount; } public void setLowStockCount(long v){ lowStockCount=v; }
    public long getPendingRequests(){ return pendingRequests; } public void setPendingRequests(long v){ pendingRequests=v; }
    public BigDecimal getTotalSalesThisMonth(){ return totalSalesThisMonth; } public void setTotalSalesThisMonth(BigDecimal v){ totalSalesThisMonth=v; }
    public BigDecimal getTotalPurchasesThisMonth(){ return totalPurchasesThisMonth; } public void setTotalPurchasesThisMonth(BigDecimal v){ totalPurchasesThisMonth=v; }
    public long getTotalTransactions(){ return totalTransactions; } public void setTotalTransactions(long v){ totalTransactions=v; }
}
