package com.maolintu.flashsale.controller;


import com.maolintu.flashsale.result.CodeMsg;
import com.maolintu.flashsale.result.Result;
import com.maolintu.flashsale.service.RedisService;
import com.maolintu.flashsale.service.SaleUserService;
import com.maolintu.flashsale.service.UserService;
import com.maolintu.flashsale.util.ValidatorUtil;
import com.maolintu.flashsale.vo.LoginVo;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/login")
public class LoginController {

  private static Logger log = LoggerFactory.getLogger(LoginController.class);

  @Autowired
  SaleUserService saleUserService;

  @Autowired
  RedisService redisService;

  @RequestMapping("/to_login")
  public String toLogin(Model model){
    return "login";
  }


  @RequestMapping("/do_login")
  @ResponseBody
  public Result<String> doLogin(HttpServletResponse response, @Valid LoginVo loginVo) {
    log.info(loginVo.toString());

    String token = saleUserService.login(response, loginVo);

    return Result.success(token);

  }

}
