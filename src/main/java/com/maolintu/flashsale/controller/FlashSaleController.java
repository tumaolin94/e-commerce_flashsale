package com.maolintu.flashsale.controller;


import com.maolintu.flashsale.domain.FlashsaleOrder;
import com.maolintu.flashsale.domain.OrderInfo;
import com.maolintu.flashsale.domain.SaleUser;
import com.maolintu.flashsale.result.CodeMsg;
import com.maolintu.flashsale.result.Result;
import com.maolintu.flashsale.service.FlashSaleService;
import com.maolintu.flashsale.service.GoodsService;
import com.maolintu.flashsale.service.OrderService;
import com.maolintu.flashsale.service.RedisService;
import com.maolintu.flashsale.service.SaleUserService;
import com.maolintu.flashsale.vo.GoodsVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/sale")
public class FlashSaleController {


  @Autowired
  SaleUserService saleUserService;

  @Autowired
  RedisService redisService;

  @Autowired
  GoodsService goodsService;

  @Autowired
  OrderService orderService;

  @Autowired
  FlashSaleService flashSaleService;

  private static Logger logger = LoggerFactory.getLogger(FlashSaleController.class);

  @PostMapping("/do_buy")
  @ResponseBody
  public Result<OrderInfo> doBuy(Model model, SaleUser user, @RequestParam("goodsId") long goodsId){

    model.addAttribute("user", user);
    if(user == null){
//      return "login";
      return Result.error(CodeMsg.SESSION_ERROR);
    }

    GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
    int stock = goods.getStockCount();
    if(stock <= 0){
//      model.addAttribute("errmsg", CodeMsg.SALE_OVER.getMsg());
//      return "buy_fail";
      return Result.error(CodeMsg.SALE_OVER);
    }

    FlashsaleOrder order = orderService.getSaleOrderByUserIdGoodsId(user.getId(), goodsId);

    if(order != null){
//      model.addAttribute("errmsg", CodeMsg.REPEATE_ORDER.getMsg());
//      return "buy_fail";
      return Result.error(CodeMsg.REPEATE_ORDER);
    }

    //decrease stock, make an order
    OrderInfo orderInfo = flashSaleService.completeOrder(user, goods);

//    model.addAttribute("orderInfo", orderInfo);
//    model.addAttribute("goods", goods);

//    return "order_detail2";
    return Result.success(orderInfo);
  }

}
