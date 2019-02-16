package com.maolintu.flashsale.controller;


import com.maolintu.flashsale.domain.FlashsaleOrder;
import com.maolintu.flashsale.domain.OrderInfo;
import com.maolintu.flashsale.domain.SaleUser;
import com.maolintu.flashsale.rabbitmq.FlashSaleMessage;
import com.maolintu.flashsale.rabbitmq.MQSender;
import com.maolintu.flashsale.redis.GoodsKey;
import com.maolintu.flashsale.result.CodeMsg;
import com.maolintu.flashsale.result.Result;
import com.maolintu.flashsale.service.FlashSaleService;
import com.maolintu.flashsale.service.GoodsService;
import com.maolintu.flashsale.service.OrderService;
import com.maolintu.flashsale.service.RedisService;
import com.maolintu.flashsale.service.SaleUserService;
import com.maolintu.flashsale.vo.GoodsVo;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/sale")
public class FlashSaleController implements InitializingBean {


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

  @Autowired
  MQSender sender;

  private static Logger logger = LoggerFactory.getLogger(FlashSaleController.class);

  private Map<Long, Boolean> localOverMap = new HashMap<>();

  @PostMapping("/do_buy")
  @ResponseBody
  public Result<Integer> doBuy(Model model, SaleUser user, @RequestParam("goodsId") long goodsId){

    model.addAttribute("user", user);
    logger.info("user = {}", user);
    if(user == null){
//      return "login";
      return Result.error(CodeMsg.SESSION_ERROR);
    }

    if(localOverMap.get(goodsId)){
      return Result.error(CodeMsg.SALE_OVER);
    }

    long stock = redisService.decr(GoodsKey.getGoodsStock, "" + goodsId);

    // get stock by redis
    if(stock < 0){
      localOverMap.put(goodsId, true);
      return Result.error(CodeMsg.SALE_OVER);
    }
    FlashsaleOrder order = orderService.getSaleOrderByUserIdGoodsId(user.getId(), goodsId);

    if(order != null){
      return Result.error(CodeMsg.REPEATE_ORDER);
    }

    //enque
    FlashSaleMessage message = new FlashSaleMessage();

    message.setUser(user);
    message.setGoodsId(goodsId);
    sender.sendFlashSaleMessage(message);

    return Result.success(0);
//    GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
//    int stock = goods.getStockCount();
//    if(stock <= 0){
//      return Result.error(CodeMsg.SALE_OVER);
//    }
//
//    FlashsaleOrder order = orderService.getSaleOrderByUserIdGoodsId(user.getId(), goodsId);
//
//    if(order != null){
//      return Result.error(CodeMsg.REPEATE_ORDER);
//    }
//
//    //decrease stock, make an order
//    OrderInfo orderInfo = flashSaleService.completeOrder(user, goods);
//
//
//    return Result.success(orderInfo);

  }

  /**
   * orderId：success
   * -1：fail
   * 0： in queue
   * */
  @RequestMapping(value="/result", method= RequestMethod.GET)
  @ResponseBody
  public Result<Long> flashSaleResult(Model model,SaleUser user,
      @RequestParam("goodsId")long goodsId) {
    model.addAttribute("user", user);
    if(user == null) {
      return Result.error(CodeMsg.SESSION_ERROR);
    }
    long result  =flashSaleService.getResult(user.getId(), goodsId);
    return Result.success(result);
  }

  /**
   *
   * Initialization
   * */
  @Override
  public void afterPropertiesSet() throws Exception {
    List<GoodsVo> goodsList = goodsService.listGoodsVo();
    if(goodsList == null){
      return;
    }
    for(GoodsVo goodsVo: goodsList){
      redisService.set(GoodsKey.getGoodsStock,""+ goodsVo.getId(), goodsVo.getStockCount());
      localOverMap.put(goodsVo.getId(), false);
    }
  }
}
