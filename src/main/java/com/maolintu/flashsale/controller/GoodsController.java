package com.maolintu.flashsale.controller;

import com.maolintu.flashsale.domain.SaleUser;
import com.maolintu.flashsale.redis.GoodsKey;
import com.maolintu.flashsale.service.GoodsService;
import com.maolintu.flashsale.service.RedisService;
import com.maolintu.flashsale.service.SaleUserService;
import com.maolintu.flashsale.vo.GoodsVo;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.spring4.context.SpringWebContext;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;

@Controller
@RequestMapping("/goods")
public class GoodsController {

  @Autowired
  SaleUserService saleUserService;

  @Autowired
  RedisService redisService;

  @Autowired
  GoodsService goodsService;

  @Autowired
  ThymeleafViewResolver thymeleafViewResolver;

  @Autowired
  ApplicationContext applicationContext;




  private static Logger logger = LoggerFactory.getLogger(GoodsController.class);
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

  @RequestMapping(value = "/to_list", produces = "text/html")
  @ResponseBody
  public String toList(HttpServletRequest request, HttpServletResponse response, Model model, SaleUser user){



    // get the list of goods;
    List<GoodsVo> goodsList = goodsService.listGoodsVo();


    model.addAttribute("user", user);
    model.addAttribute("goodsList", goodsList);


//    return "goods_list";

    String html = redisService.get(GoodsKey.getGoodsList, "", String.class);

    //catch
    if(!StringUtils.isEmpty(html)){
      logger.info("return page from catch");
      return html;
    }else{

      // render
      SpringWebContext springWebContext = new SpringWebContext(request, response, request.getServletContext(), request.getLocale(),
          model.asMap(), applicationContext);

      html = thymeleafViewResolver.getTemplateEngine().process("goods_list" , springWebContext);

      if(!StringUtils.isEmpty(html)){
        redisService.set(GoodsKey.getGoodsList, "", html);
      }
      return html;
    }
  }

  @RequestMapping(value = "/to_detail/{goodsId}", produces = "text/html")
  @ResponseBody
  public String detail(HttpServletRequest request, HttpServletResponse response, Model model, SaleUser user, @PathVariable("goodsId") long goodsId){

    model.addAttribute("user", user);

    String html = redisService.get(GoodsKey.getGoodsDetail, ""+goodsId, String.class);

    //catch
    if(!StringUtils.isEmpty(html)){
      logger.info("return page from catch");
      return html;
    }

    GoodsVo goodsVo = goodsService.getGoodsVoByGoodsId(goodsId);


    model.addAttribute("goods", goodsVo);

    long startTime = goodsVo.getStartDate().getTime();
    long endTime = goodsVo.getEndDate().getTime();
    long curTime = System.currentTimeMillis();

    long remainSeconds = 0;

    int flashSaleStatus = 0;
    if(curTime < startTime ){ //countDown
      flashSaleStatus = 0;

      remainSeconds = (startTime - curTime) / 1000;


    }else if(curTime > endTime ){// End
      flashSaleStatus = 2;
      remainSeconds = - 1;
    }else{// Being
      flashSaleStatus = 1;
      remainSeconds = 0;
    }

    model.addAttribute("flashSaleStatus", flashSaleStatus);
    model.addAttribute("remainSeconds", remainSeconds);

    logger.info("user = {}, goods = {}, flashSaleStatus = {}, , remainSeconds = {}", user, goodsVo, flashSaleStatus, remainSeconds);
    logger.info("startTime = {}, endTime = {}, curTime = {}, , remainSeconds = {}", startTime, endTime, curTime, remainSeconds);

//    return "goods_detail";

    // render
    SpringWebContext springWebContext = new SpringWebContext(request, response, request.getServletContext(), request.getLocale(),
        model.asMap(), applicationContext);

    html = thymeleafViewResolver.getTemplateEngine().process("goods_detail" , springWebContext);

    if(!StringUtils.isEmpty(html)){
      redisService.set(GoodsKey.getGoodsDetail, ""+goodsId, html);
    }
    return html;

  }
}
