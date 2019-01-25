package com.maolintu.flashsale.vo;

import com.maolintu.flashsale.domain.Goods;
import java.util.Date;

public class GoodsVo extends Goods {
  private Double salePrice;
  private Integer stockCount;
  private Date startDate;
  private Date endDate;
  public Integer getStockCount() {
    return stockCount;
  }
  public void setStockCount(Integer stockCount) {
    this.stockCount = stockCount;
  }
  public Date getStartDate() {
    return startDate;
  }
  public void setStartDate(Date startDate) {
    this.startDate = startDate;
  }
  public Date getEndDate() {
    return endDate;
  }
  public void setEndDate(Date endDate) {
    this.endDate = endDate;
  }

  public Double getSalePrice() {
    return salePrice;
  }

  public void setSalePrice(Double salePrice) {
    this.salePrice = salePrice;
  }

  @Override
  public String toString() {
    return "GoodsVo{" +
        "salePrice=" + salePrice +
        ", stockCount=" + stockCount +
        ", startDate=" + startDate +
        ", endDate=" + endDate +
        '}';
  }
}
