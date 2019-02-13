package com.maolintu.flashsale.redis;

public class SaleKey extends BasePrefix{

	private SaleKey( int expireSeconds, String prefix) {
		super(expireSeconds, prefix);
	}
	public static SaleKey isGoodsOver = new SaleKey(0, "go");
	public static SaleKey getSalePath = new SaleKey(60, "sp");
	public static SaleKey getSaleVerifyCode = new SaleKey(300, "vc");
}
