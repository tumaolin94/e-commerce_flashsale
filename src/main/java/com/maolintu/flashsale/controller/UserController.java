package com.maolintu.flashsale.controller;

import com.maolintu.flashsale.domain.SaleUser;
import com.maolintu.flashsale.result.Result;
import com.maolintu.flashsale.service.RedisService;
import com.maolintu.flashsale.service.SaleUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/user")
public class UserController {

  @Autowired
  SaleUserService userService;

  @Autowired
  RedisService redisService;

  private static Logger logger = LoggerFactory.getLogger(FlashSaleController.class);

  @RequestMapping("/info")
  @ResponseBody
  public Result<SaleUser> info(Model model, SaleUser user) {

    logger.info("request info {}", user);

    return Result.success(user);
  }

}
