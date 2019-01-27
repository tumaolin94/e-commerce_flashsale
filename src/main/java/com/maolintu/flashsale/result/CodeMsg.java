package com.maolintu.flashsale.result;

public class CodeMsg {

  private int code;
  private String msg;

  //General
  public static CodeMsg SUCCESS = new CodeMsg(0, "success");
  public static CodeMsg SERVER_ERROR = new CodeMsg(500100, "SERVER_ERROR");
  public static CodeMsg BIND_ERROR = new CodeMsg(500101, "BIND_ERROR：%s");
  public static CodeMsg REQUEST_ILLEGAL = new CodeMsg(500102, "REQUEST_ILLEGAL");
  public static CodeMsg ACCESS_LIMIT_REACHED= new CodeMsg(500104, "ACCESS_LIMIT_REACHED");
  //Login 5002XX
  public static CodeMsg SESSION_ERROR = new CodeMsg(500210, "Session_NOT_EXISTS");
  public static CodeMsg PASSWORD_EMPTY = new CodeMsg(500211, "PASSWORD_EMPTY");
  public static CodeMsg MOBILE_EMPTY = new CodeMsg(500212, "MOBILE_EMPTY");
  public static CodeMsg MOBILE_ERROR = new CodeMsg(500213, "MOBILE_ERROR");
  public static CodeMsg MOBILE_NOT_EXIST = new CodeMsg(500214, "MOBILE_NOT_EXIST");
  public static CodeMsg PASSWORD_ERROR = new CodeMsg(500215, "PASSWORD_ERROR");


  //Goods 5003XX


  //Orders 5004XX
  public static CodeMsg ORDER_NOT_EXIST = new CodeMsg(500400, "ORDER_NOT_EXIST");

  //Flashsale 5005XX
  public static CodeMsg SALE_OVER = new CodeMsg(500500, "SALE_OVER_OVER");
  public static CodeMsg REPEATE_ORDER = new CodeMsg(500501, "REPEATE_ORDER");
  public static CodeMsg BUY_FAIL = new CodeMsg(500502, "BUY_FAIL");


  private CodeMsg( ) {
  }

  private CodeMsg( int code,String msg ) {
    this.code = code;
    this.msg = msg;
  }

  public int getCode() {
    return code;
  }
  public void setCode(int code) {
    this.code = code;
  }
  public String getMsg() {
    return msg;
  }
  public void setMsg(String msg) {
    this.msg = msg;
  }

  public CodeMsg fillArgs(Object... args) {
    int code = this.code;
    String message = String.format(this.msg, args);
    return new CodeMsg(code, message);
  }

  @Override
  public String toString() {
    return "CodeMsg [code=" + code + ", msg=" + msg + "]";
  }


}

