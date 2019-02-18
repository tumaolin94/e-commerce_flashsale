package com.maolintu.flashsale.service;

import com.maolintu.flashsale.dao.GoodsDao;
import com.maolintu.flashsale.domain.FlashsaleOrder;
import com.maolintu.flashsale.domain.Goods;
import com.maolintu.flashsale.domain.OrderInfo;
import com.maolintu.flashsale.domain.SaleUser;
import com.maolintu.flashsale.redis.SaleKey;
import com.maolintu.flashsale.util.MD5Util;
import com.maolintu.flashsale.util.UUIDUtil;
import com.maolintu.flashsale.vo.GoodsVo;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FlashSaleService {

  @Autowired
  GoodsService goodsService;

  @Autowired
  OrderService orderService;

  @Autowired
  RedisService redisService;

  @Transactional
  public OrderInfo completeOrder(SaleUser user, GoodsVo goods) {

    boolean success = goodsService.reduceStock(goods);
    if(success){
      OrderInfo orderInfo = orderService.createOrder(user, goods);

      return orderInfo;
    }else{
      setGoodsOver(goods.getId());
      return null;
    }

  }

  public long getResult(Long userId, long goodsId) {
    FlashsaleOrder order = orderService.getSaleOrderByUserIdGoodsId(userId, goodsId);
    if(order != null) {//success
      return order.getOrderId();
    }else {
      boolean isOver = getGoodsOver(goodsId);
      if(isOver) {
        return -1;
      }else {
        return 0;
      }
    }
  }

  private void setGoodsOver(Long goodsId) {
    redisService.set(SaleKey.isGoodsOver, ""+goodsId, true);
  }

  private boolean getGoodsOver(long goodsId) {
    return redisService.exists(SaleKey.isGoodsOver, ""+goodsId);
  }

  public void reset(List<GoodsVo> goodsList) {
    goodsService.resetStock(goodsList);
    orderService.deleteOrders();
  }

  public boolean checkPath(SaleUser user, long goodsId, String path) {
    if(user == null || path == null) {
      return false;
    }

    String pathOld = redisService
        .get(SaleKey.getSalePath, "" + user.getId() + "_" + goodsId, String.class);

    return path.equals(pathOld);

  }

  public String createFlashSalePath(SaleUser user, long goodsId) {
    String path = MD5Util.md5(UUIDUtil.uuid() + "123456");
    redisService.set(SaleKey.getSalePath,""+user.getId()+"_"+goodsId, path);

    return path;
  }
}
