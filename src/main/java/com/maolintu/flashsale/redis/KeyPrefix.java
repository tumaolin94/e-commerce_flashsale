package com.maolintu.flashsale.redis;

public interface KeyPrefix {

  public int expireSeconds();

  public String getPrefix();

}

