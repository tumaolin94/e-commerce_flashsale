package com.maolintu.flashsale.rabbitmq;

import com.maolintu.flashsale.service.RedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MQSender {

  private static Logger log = LoggerFactory.getLogger(MQSender.class);

  @Autowired
  AmqpTemplate amqpTemplate;


  public void send(Object message){
    String msg = RedisService.beanToString(message);
    amqpTemplate.convertAndSend(MQConfig.QUEUE, msg);
  }

  public void sendTopic(Object message){
    String msg = RedisService.beanToString(message);
    log.info("send topic message:"+msg);
    amqpTemplate.convertAndSend(MQConfig.TOPIC_EXCHANGE, "topic.key1", msg+"1");
    amqpTemplate.convertAndSend(MQConfig.TOPIC_EXCHANGE, "topic.key2", msg+"2");
  }
}
