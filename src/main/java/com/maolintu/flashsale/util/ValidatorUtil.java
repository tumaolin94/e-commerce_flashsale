package com.maolintu.flashsale.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

public class ValidatorUtil {

  private static final Pattern MOBILE_PATTERN = Pattern.compile("1\\d{10}");

  public static boolean isMobile(String src) {
    if(StringUtils.isEmpty(src)) {
      return false;
    }
    Matcher m = MOBILE_PATTERN.matcher(src);
    return m.matches();
  }

	public static void main(String[] args) {
			System.out.println(isMobile("12345678901"));
			System.out.println(isMobile("1891234123"));
	}
}
