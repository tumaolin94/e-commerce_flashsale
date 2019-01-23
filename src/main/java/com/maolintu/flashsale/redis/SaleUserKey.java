package com.maolintu.flashsale.redis;

public class SaleUserKey extends BasePrefix {

  public static final int TOKEN_EXPIRE = 3600* 24 *2;

  public SaleUserKey(String prefix) {
    super(prefix);
  }

  public SaleUserKey(int expireSeconds, String prefix) {
    super(expireSeconds, prefix);
  }

  public static SaleUserKey token = new SaleUserKey(TOKEN_EXPIRE,"tk");
}
