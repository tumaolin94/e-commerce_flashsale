package com.maolintu.flashsale.rabbitmq;


import com.maolintu.flashsale.domain.FlashsaleOrder;
import com.maolintu.flashsale.domain.SaleUser;
import com.maolintu.flashsale.service.FlashSaleService;
import com.maolintu.flashsale.service.GoodsService;
import com.maolintu.flashsale.service.OrderService;
import com.maolintu.flashsale.service.RedisService;
import com.maolintu.flashsale.vo.GoodsVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MQReceiver {

  private static Logger log = LoggerFactory.getLogger(MQReceiver.class);

  @Autowired
  GoodsService goodsService;

  @Autowired
  OrderService orderService;

  @Autowired
  FlashSaleService flashSaleService;

  @RabbitListener(queues=MQConfig.QUEUE)
  public void testReceive(String message) {

    log.info("receive message: {}",message);
  }

  @RabbitListener(queues=MQConfig.TOPIC_QUEUE1)
  public void receiveTopic1(String message) {

    log.info("receive message topic queue 1: {}",message);
  }

  @RabbitListener(queues=MQConfig.TOPIC_QUEUE2)
  public void receiveTopic2(String message) {

    log.info("receive message topic queue 2: {}",message);
  }

		@RabbitListener(queues=MQConfig.HEADER_QUEUE)
		public void receiveHeaderQueue(byte[] message) {
			log.info(" header  queue message:"+new String(message));
		}

  @RabbitListener(queues=MQConfig.SALE_QUEUE)
  public void receiveSaleQueue(String message) {

    log.info("receiveSaleQueue message: {}",message);
    FlashSaleMessage fm = RedisService.stringToBean(message, FlashSaleMessage.class);
    SaleUser user = fm.getUser();
    long goodsId = fm.getGoodsId();

    GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
    int stock = goods.getStockCount();
    if(stock <= 0) {
      return;
    }
    //if the user get the order successfully!
    FlashsaleOrder order = orderService
        .getSaleOrderByUserIdGoodsId(user.getId(), goodsId);
    if(order != null) {
      return;
    }
    //complete order
    flashSaleService.completeOrder(user, goods);

  }
}
