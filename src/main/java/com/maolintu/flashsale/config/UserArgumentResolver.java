package com.maolintu.flashsale.config;

import com.maolintu.flashsale.controller.GoodsController;
import com.maolintu.flashsale.domain.SaleUser;
import com.maolintu.flashsale.service.SaleUserService;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Service
public class UserArgumentResolver implements HandlerMethodArgumentResolver {

	@Autowired
	SaleUserService userService;

	private static Logger logger = LoggerFactory.getLogger(UserArgumentResolver.class);

	public boolean supportsParameter(MethodParameter parameter) {
		Class<?> clazz = parameter.getParameterType();
		return clazz==SaleUser.class;
	}

	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

		HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
		HttpServletResponse response = webRequest.getNativeResponse(HttpServletResponse.class);

		String paramToken = request.getParameter(SaleUserService.COOKIE_NAME_TOKEN);
		String cookieToken = getcookieValue(request, SaleUserService.COOKIE_NAME_TOKEN);
		if(StringUtils.isEmpty(paramToken) && StringUtils.isEmpty(cookieToken)){
			return null;
		}

		String token = StringUtils.isEmpty(paramToken)?cookieToken:paramToken;
		SaleUser user = userService.getByToken(response, token);

		return user;
	}

	private String getcookieValue(HttpServletRequest request, String cookieNameToken) {
		Cookie[] cookies = request.getCookies();
		if(cookies == null || cookies.length <= 0){
			return null;
		}
		logger.info("cookies size = {}", cookies.length);
		for(Cookie cookie: cookies){
			if(cookie.getName().equals(cookieNameToken)){
				return cookie.getValue();
			}
		}

		return null;
	}


}
