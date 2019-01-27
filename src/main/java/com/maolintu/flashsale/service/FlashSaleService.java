package com.maolintu.flashsale.service;

import com.maolintu.flashsale.dao.GoodsDao;
import com.maolintu.flashsale.domain.Goods;
import com.maolintu.flashsale.domain.OrderInfo;
import com.maolintu.flashsale.domain.SaleUser;
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


  @Transactional
  public OrderInfo completeOrder(SaleUser user, GoodsVo goods) {

    goodsService.reduceStock(goods);

    OrderInfo orderInfo = orderService.createOrder(user, goods);

    return orderInfo;
  }
}
