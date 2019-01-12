package com.maolintu.flashsale.controller;

import com.maolintu.flashsale.domain.User;
import com.maolintu.flashsale.result.Result;
import com.maolintu.flashsale.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/demo")
public class SampleController {

  @Autowired
  UserService userService;

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


}
