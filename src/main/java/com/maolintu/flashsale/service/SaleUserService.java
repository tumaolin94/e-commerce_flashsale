package com.maolintu.flashsale.service;

import com.maolintu.flashsale.controller.LoginController;
import com.maolintu.flashsale.dao.SaleUserDao;
import com.maolintu.flashsale.domain.SaleUser;
import com.maolintu.flashsale.result.CodeMsg;
import com.maolintu.flashsale.util.MD5Util;
import com.maolintu.flashsale.vo.LoginVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SaleUserService {

  @Autowired
  SaleUserDao saleUserDao;

  private static Logger log = LoggerFactory.getLogger(SaleUserService.class);

  public SaleUser getById(long id){
    return saleUserDao.getById(id);
  }

  public CodeMsg login(LoginVo loginVo) {
    if(loginVo == null){
      return CodeMsg.SERVER_ERROR;
    }

    String mobile = loginVo.getMobile();
    String password = loginVo.getPassword();

    SaleUser user = getById(Long.parseLong(mobile));
    if(user == null){
      return CodeMsg.MOBILE_NOT_EXIST;
    }

    String dbPass = user.getPassword();
    String dbSalt = user.getSalt();
    String calcPass = MD5Util.formPassToDBPass(password, dbSalt);
    log.info("dbPass= {}, dbSalt = {}, password = {},calcPass = {}", dbPass, dbSalt, password, calcPass);
    if(!calcPass.equals(dbPass)){
      return CodeMsg.PASSWORD_ERROR;
    }
    return CodeMsg.SUCCESS;
  }
}
