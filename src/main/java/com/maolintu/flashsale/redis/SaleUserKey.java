package com.maolintu.flashsale.redis;

public class SaleUserKey extends BasePrefix {

  public SaleUserKey(String prefix) {
    super(prefix);
  }

  public static SaleUserKey token = new SaleUserKey("tk");
}
