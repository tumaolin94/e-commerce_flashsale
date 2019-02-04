package com.maolintu.flashsale.vo;

import com.maolintu.flashsale.domain.SaleUser;

public class GoodsDetailVo {

  private int flashSaleStatus = 0;
  private long remainSeconds = 0;
  private GoodsVo goods ;
  private SaleUser user;

  public int getFlashSaleStatus() {
    return flashSaleStatus;
  }

  public void setFlashSaleStatus(int flashSaleStatus) {
    this.flashSaleStatus = flashSaleStatus;
  }

  public long getRemainSeconds() {
    return remainSeconds;
  }

  public void setRemainSeconds(long remainSeconds) {
    this.remainSeconds = remainSeconds;
  }

  public GoodsVo getGoods() {
    return goods;
  }

  public void setGoods(GoodsVo goods) {
    this.goods = goods;
  }

  public SaleUser getUser() {
    return user;
  }

  public void setUser(SaleUser user) {
    this.user = user;
  }
}
