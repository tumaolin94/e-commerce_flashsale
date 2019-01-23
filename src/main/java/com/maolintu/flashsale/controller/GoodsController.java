package com.maolintu.flashsale.controller;

import com.maolintu.flashsale.domain.SaleUser;
import com.maolintu.flashsale.service.GoodsService;
import com.maolintu.flashsale.service.RedisService;
import com.maolintu.flashsale.service.SaleUserService;
import com.maolintu.flashsale.vo.GoodsVo;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/goods")
public class GoodsController {

  @Autowired
  SaleUserService saleUserService;

  @Autowired
  RedisService redisService;

  @Autowired
  GoodsService goodsService;
/**
 * 0.1 Version
 *
 * */
//  @RequestMapping("/to_list")
//  public String toLogin(HttpServletResponse response, Model model, @CookieValue(value = SaleUserService.COOKIE_NAME_TOKEN, required = false) String cookieToken,
//      @RequestParam(value=SaleUserService.COOKIE_NAME_TOKEN, required = false) String paramToken){
//
//    if(StringUtils.isEmpty(paramToken) && StringUtils.isEmpty(cookieToken)){
//      return "login";
//    }
//
//    String token = StringUtils.isEmpty(paramToken)?cookieToken:paramToken;
//    SaleUser user = saleUserService.getByToken(response, token);
//    model.addAttribute("user", user);
//    return "goods_list";
//  }

  @RequestMapping("/to_list")
  public String toLogin(Model model, SaleUser user){

    model.addAttribute("user", user);

    // get the list of goods;
    List<GoodsVo> goodsList = goodsService.listGoodsVo();


    model.addAttribute("goodsList", goodsList);

    return "goods_list2";
  }

//  @RequestMapping("/to_detail")
//  public String detail(HttpServletResponse response, Model model, @CookieValue(value = SaleUserService.COOKIE_NAME_TOKEN, required = false) String cookieToken,
//      @RequestParam(value=SaleUserService.COOKIE_NAME_TOKEN, required = false) String paramToken){
//
//    if(StringUtils.isEmpty(paramToken) && StringUtils.isEmpty(cookieToken)){
//      return "login";
//    }
//
//    String token = StringUtils.isEmpty(paramToken)?cookieToken:paramToken;
//    SaleUser user = saleUserService.getByToken(response, token);
//    model.addAttribute("user", user);
//    return "goods_list";
//  }
}
