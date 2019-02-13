package com.maolintu.flashsale.rabbitmq;

import com.maolintu.flashsale.domain.SaleUser;

public class FlashSaleMessage {
  private SaleUser user;
  private long goodsId;

  public SaleUser getUser() {
    return user;
  }

  public void setUser(SaleUser user) {
    this.user = user;
  }

  public long getGoodsId() {
    return goodsId;
  }

  public void setGoodsId(long goodsId) {
    this.goodsId = goodsId;
  }
}
