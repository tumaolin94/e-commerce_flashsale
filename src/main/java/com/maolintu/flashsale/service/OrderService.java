package com.maolintu.flashsale.service;

import com.maolintu.flashsale.dao.GoodsDao;
import com.maolintu.flashsale.dao.OrderDao;
import com.maolintu.flashsale.domain.FlashsaleOrder;
import com.maolintu.flashsale.domain.OrderInfo;
import com.maolintu.flashsale.domain.SaleUser;
import com.maolintu.flashsale.redis.OrderKey;
import com.maolintu.flashsale.vo.GoodsVo;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

  @Autowired
  OrderDao orderDao;

  @Autowired
  RedisService redisService;

  public FlashsaleOrder getSaleOrderByUserIdGoodsId(long userId, long goodsId) {
//    return orderDao.getSaleOrderByUserIdGoodsId(userId, goodsId);
    return redisService.get(OrderKey.getSaleOrderByUidGid, userId+"_"+goodsId, FlashsaleOrder.class);

  }

  @Transactional
  public OrderInfo createOrder(SaleUser user, GoodsVo goods) {
    OrderInfo orderInfo = new OrderInfo();
    orderInfo.setCreateDate(new Date());
    orderInfo.setDeliveryAddrId(0L);
    orderInfo.setGoodsCount(1);
    orderInfo.setGoodsId(goods.getId());
    orderInfo.setGoodsName(goods.getGoodsName());
    orderInfo.setGoodsPrice(goods.getSalePrice());
    orderInfo.setOrderChannel(1);
    orderInfo.setStatus(0);
    orderInfo.setUserId(user.getId());
    orderDao.insert(orderInfo); //
    FlashsaleOrder flashsaleOrder = new FlashsaleOrder();
    flashsaleOrder.setGoodsId(goods.getId());
    flashsaleOrder.setOrderId(orderInfo.getGoodsId());
    flashsaleOrder.setUserId(user.getId());
    orderDao.insertFlashsaleOrder(flashsaleOrder);

    redisService.set(OrderKey.getSaleOrderByUidGid, user.getId()+"_"+goods.getId(), flashsaleOrder);

    return orderInfo;
  }


  public OrderInfo getOrderById(long orderId) {
    return orderDao.getOrderById(orderId);
  }
}
