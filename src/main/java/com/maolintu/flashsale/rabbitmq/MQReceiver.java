package com.maolintu.flashsale.rabbitmq;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class MQReceiver {

  private static Logger log = LoggerFactory.getLogger(MQReceiver.class);

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
}
