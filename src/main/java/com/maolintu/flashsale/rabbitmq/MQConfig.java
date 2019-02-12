package com.maolintu.flashsale.rabbitmq;

import java.util.HashMap;
import java.util.Map;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.HeadersExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MQConfig {

  public static final String QUEUE = "queue";
  public static final String TOPIC_QUEUE1 = "topic.queue1";
  public static final String TOPIC_QUEUE2 = "topic.queue2";
  public static final String TOPIC_EXCHANGE = "topicExchange";
  public static final String FANOUT_EXCHANGE = "fanoutExchange";
  public static final String HEADERS_EXCHANGE = "headerExchange";
  public static final String HEADER_QUEUE = "headerQueue";


  /**
   *
   * Direct exchange
   * */
  @Bean
  public Queue queue(){
    return new Queue(QUEUE, true);
  }

  /**
   *
   * Topic exchange
   * */

  @Bean
  public Queue topicQueue1(){
    return new Queue(TOPIC_QUEUE1, true);
  }

  @Bean
  public Queue topicQueue2(){
    return new Queue(TOPIC_QUEUE2, true);
  }

  @Bean
  public TopicExchange topicExchange(){
    return new TopicExchange(TOPIC_EXCHANGE);
  }

  @Bean
  public Binding topicBinding1(){
    return BindingBuilder.bind(topicQueue1()).to(topicExchange()).with("topic.key1");
  }

  @Bean
  public Binding topicBinding2(){
    return BindingBuilder.bind(topicQueue2()).to(topicExchange()).with("topic.#");
  }

  /**
   *
   * Fanout exchange
   * */
  @Bean
  public FanoutExchange fanoutExchange(){
    return new FanoutExchange(FANOUT_EXCHANGE);
  }
  @Bean
  public Binding fanoutBinding1(){
    return BindingBuilder.bind(topicQueue1()).to(fanoutExchange());
  }

  @Bean
  public Binding fanoutBinding2(){
    return BindingBuilder.bind(topicQueue2()).to(fanoutExchange());
  }

  /**
   *
   * Header exchange
   * */
  @Bean
  public HeadersExchange headersExchange(){
    return new HeadersExchange(HEADERS_EXCHANGE);
  }

  @Bean
  public Queue headerQueue(){
    return new Queue(HEADER_QUEUE, true);
  }

  @Bean
  public Binding headerBinding(){
    Map<String, Object> map = new HashMap<>();
    map.put("header1","value1");
    map.put("header2","value2");

    return BindingBuilder.bind(headerQueue()).to(headersExchange()).whereAll(map).match();
  }
}
