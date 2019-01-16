package com.maolintu.flashsale.controller;


import com.maolintu.flashsale.result.CodeMsg;
import com.maolintu.flashsale.result.Result;
import com.maolintu.flashsale.service.RedisService;
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
  UserService userService;

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
    String passInput = loginVo.getPassword();
    String mobile = loginVo.getMobile();
    if(StringUtils.isEmpty(passInput)){
      return Result.error(CodeMsg.PASSWORD_EMPTY);
    }
    if(StringUtils.isEmpty(mobile)){
      return Result.error(CodeMsg.MOBILE_EMPTY);
    }
    if(ValidatorUtil.isMobile(mobile)){
      return Result.error(CodeMsg.MOBILE_ERROR);
    }
//    String token = userService.login(response, loginVo);
    return Result.success(loginVo.toString());
  }

}
