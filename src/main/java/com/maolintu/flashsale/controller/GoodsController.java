package com.maolintu.flashsale.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/goods")
public class GoodsController {

  @RequestMapping("/to_list")
  public String toLogin(){
    return "goods_list";
  }
}
