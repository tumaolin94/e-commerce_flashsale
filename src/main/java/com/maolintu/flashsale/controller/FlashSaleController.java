package com.maolintu.flashsale.controller;


import com.maolintu.flashsale.domain.FlashsaleOrder;
import com.maolintu.flashsale.domain.OrderInfo;
import com.maolintu.flashsale.domain.SaleUser;
import com.maolintu.flashsale.rabbitmq.FlashSaleMessage;
import com.maolintu.flashsale.rabbitmq.MQSender;
import com.maolintu.flashsale.redis.AccessKey;
import com.maolintu.flashsale.redis.GoodsKey;
import com.maolintu.flashsale.redis.OrderKey;
import com.maolintu.flashsale.redis.SaleKey;
import com.maolintu.flashsale.result.CodeMsg;
import com.maolintu.flashsale.result.Result;
import com.maolintu.flashsale.service.FlashSaleService;
import com.maolintu.flashsale.service.GoodsService;
import com.maolintu.flashsale.service.OrderService;
import com.maolintu.flashsale.service.RedisService;
import com.maolintu.flashsale.service.SaleUserService;
import com.maolintu.flashsale.util.MD5Util;
import com.maolintu.flashsale.util.UUIDUtil;
import com.maolintu.flashsale.vo.GoodsVo;
import java.awt.image.BufferedImage;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
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

  @PostMapping("/{path}/do_buy")
  @ResponseBody
  public Result<Integer> doBuy(Model model, SaleUser user, @RequestParam("goodsId") long goodsId,
      @PathVariable("path") String path){

    model.addAttribute("user", user);
    logger.info("user = {}", user);
    if(user == null){
//      return "login";
      return Result.error(CodeMsg.SESSION_ERROR);
    }

    // check path
    boolean isPath = flashSaleService.checkPath(user, goodsId, path);

    if(!isPath){
      return Result.error(CodeMsg.REQUEST_ILLEGAL);
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

  @RequestMapping(value="/path", method=RequestMethod.GET)
  @ResponseBody
  public Result<String> getFlashSalePath(HttpServletRequest request, SaleUser user,
      @RequestParam("goodsId")long goodsId,
      @RequestParam(value = "verifyCode", required = false)String verifyCode
  ) {
    if(user == null) {
      return Result.error(CodeMsg.SESSION_ERROR);
    }

    //visit times
    String uri = request.getRequestURI();
    String key = uri + "_" + user.getId();
    Integer count = redisService.get(AccessKey.access, key, Integer.class);
    if(count == null) {
      redisService.set(AccessKey.access, key, 1);
    }else if( count < 5){
      redisService.incr(AccessKey.access, key);
    }else{
      return Result.error(CodeMsg.ACCESS_LIMIT_REACHED);
    }

    if(StringUtils.isEmpty(verifyCode)|| !StringUtils.isNumeric(verifyCode)){
      return Result.error(CodeMsg.REQUEST_ILLEGAL);
    }

    boolean check = flashSaleService.checkVerifyCode(user, goodsId, Integer.parseInt(verifyCode));
    if(!check) {
      return Result.error(CodeMsg.REQUEST_ILLEGAL);
    }
    String path  =flashSaleService.createFlashSalePath(user, goodsId);

    return Result.success(path);
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

  @RequestMapping(value="/reset", method=RequestMethod.GET)
  @ResponseBody
  public Result<Boolean> reset(Model model) {
    List<GoodsVo> goodsList = goodsService.listGoodsVo();
    for(GoodsVo goods : goodsList) {
      goods.setStockCount(10);
      redisService.set(GoodsKey.getGoodsStock, ""+goods.getId(), 10);
      localOverMap.put(goods.getId(), false);
    }
    redisService.delete(OrderKey.getSaleOrderByUidGid);
    redisService.delete(SaleKey.isGoodsOver);
    flashSaleService.reset(goodsList);
    return Result.success(true);
  }

  @RequestMapping(value="/verifyCode", method=RequestMethod.GET)
  @ResponseBody
  public Result<String> getMiaoshaVerifyCod(HttpServletResponse response,SaleUser user,
      @RequestParam("goodsId")long goodsId) {
    if(user == null) {
      return Result.error(CodeMsg.SESSION_ERROR);
    }
    try {
      BufferedImage image  = flashSaleService.createVerifyCode(user, goodsId);
      OutputStream out = response.getOutputStream();
      ImageIO.write(image, "JPEG", out);
      out.flush();
      out.close();
      return null;
    }catch(Exception e) {
      e.printStackTrace();
      return Result.error(CodeMsg.BUY_FAIL);
    }
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
