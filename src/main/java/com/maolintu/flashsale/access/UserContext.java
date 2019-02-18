package com.maolintu.flashsale.access;

import com.maolintu.flashsale.domain.SaleUser;

public class UserContext {
  private static ThreadLocal<SaleUser> userHolder = new ThreadLocal<SaleUser>();

  public static void setUser(SaleUser user) {
    userHolder.set(user);
  }

  public static SaleUser getUser() {
    return userHolder.get();
  }
}
