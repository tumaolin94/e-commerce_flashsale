package com.maolintu.flashsale.service;

import com.maolintu.flashsale.dao.GoodsDao;
import com.maolintu.flashsale.domain.FlashsaleOrder;
import com.maolintu.flashsale.domain.Goods;
import com.maolintu.flashsale.domain.OrderInfo;
import com.maolintu.flashsale.domain.SaleUser;
import com.maolintu.flashsale.redis.SaleKey;
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
}
