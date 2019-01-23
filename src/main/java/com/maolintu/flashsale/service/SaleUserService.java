package com.maolintu.flashsale.service;

import javax.servlet.http.Cookie;
import com.maolintu.flashsale.dao.SaleUserDao;
import com.maolintu.flashsale.domain.SaleUser;
import com.maolintu.flashsale.exception.GlobalException;
import com.maolintu.flashsale.redis.SaleUserKey;
import com.maolintu.flashsale.result.CodeMsg;
import com.maolintu.flashsale.util.MD5Util;
import com.maolintu.flashsale.util.UUIDUtil;
import com.maolintu.flashsale.vo.LoginVo;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SaleUserService {

  private static final String COOKIE_NAME_TOKEN = "token";

  @Autowired
  SaleUserDao saleUserDao;

  @Autowired
  RedisService redisService;

  private static Logger log = LoggerFactory.getLogger(SaleUserService.class);

  public SaleUser getById(long id){
    return saleUserDao.getById(id);
  }

  public boolean login(HttpServletResponse response, LoginVo loginVo) {
    if(loginVo == null){
      throw new GlobalException(CodeMsg.SERVER_ERROR);
    }

    String mobile = loginVo.getMobile();
    String password = loginVo.getPassword();

    SaleUser user = getById(Long.parseLong(mobile));
    if(user == null){
      throw new GlobalException(CodeMsg.MOBILE_NOT_EXIST);
    }

    String dbPass = user.getPassword();
    String dbSalt = user.getSalt();
    String calcPass = MD5Util.formPassToDBPass(password, dbSalt);
    log.info("dbPass= {}, dbSalt = {}, password = {},calcPass = {}", dbPass, dbSalt, password, calcPass);
    if(!calcPass.equals(dbPass)){
      throw new GlobalException(CodeMsg.PASSWORD_ERROR);
    }

    //create cookie
    String token = UUIDUtil.uuid();
    redisService.set(SaleUserKey.token, token, user);

    Cookie cookie = new Cookie(COOKIE_NAME_TOKEN, token);
    cookie.setMaxAge(SaleUserKey.token.expireSeconds());
    cookie.setPath("/");
    response.addCookie(cookie);
    return true;
  }
}
