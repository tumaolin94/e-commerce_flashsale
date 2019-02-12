package com.maolintu.flashsale.controller;

import com.maolintu.flashsale.domain.User;
import com.maolintu.flashsale.rabbitmq.MQSender;
import com.maolintu.flashsale.redis.UserKey;
import com.maolintu.flashsale.result.CodeMsg;
import com.maolintu.flashsale.result.Result;
import com.maolintu.flashsale.service.RedisService;
import com.maolintu.flashsale.service.UserService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/demo")
public class SampleController {

  @Autowired
  UserService userService;

  @Autowired
  RedisService redisService;

  @Autowired
  MQSender sender;

  @RequestMapping("/thymeleaf")
  public String thymeleaf(Model model){
    model.addAttribute("name","maolintu");

    return "hello";
  }


  @RequestMapping("/db/get")
  @ResponseBody
  public Result<User> dbGet() {
    User user = userService.getById(1);
    return Result.success(user);
  }



  @RequestMapping("/db/transaction")
  @ResponseBody
  public Result<Boolean> dbTransaction() {
    boolean result = userService.transaction(1);
    return Result.success(result);
  }


  @RequestMapping("/redis/get")
  @ResponseBody
  public Result<User> redisGet(@RequestParam("key") String key) {

    User user = redisService.get(UserKey.getById, key, User.class);
    return Result.success(user);
  }

  @RequestMapping("/redis/set")
  @ResponseBody
  public Result<Boolean> redisSet() {
    User user  = new User();
    user.setId(1);
    user.setName("1111");
    redisService.set(UserKey.getById, ""+1, user);//UserKey:id1
    return Result.success(true);
  }

  @RequestMapping("/mq")
  @ResponseBody
  public Result<String> mq() {
    sender.send("hello,");
    return Result.success("Hello，world");
  }


  @RequestMapping("/mq/topic")
  @ResponseBody
  public Result<String> mqTopic() {
    sender.sendTopic("hello,");
    return Result.success("Hello，world");
  }
//  @RequestMapping("/redis/set")
//  @ResponseBody
//  public Result<Boolean> redisSet(@RequestParam("key") String key, @RequestParam("value") String value) {
//    boolean result = redisService.set(UserKey.getById, key, value);
//    if(result){
//      return Result.success(result);
//    }else{
//      return Result.error(CodeMsg.SERVER_ERROR);
//    }
//  }

}
