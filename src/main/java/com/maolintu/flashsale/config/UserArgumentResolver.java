package com.maolintu.flashsale.config;

import com.maolintu.flashsale.domain.SaleUser;
import com.maolintu.flashsale.service.SaleUserService;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
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
			return "login";
		}

		String token = StringUtils.isEmpty(paramToken)?cookieToken:paramToken;
		SaleUser user = userService.getByToken(response, token);

		return user;
	}

	private String getcookieValue(HttpServletRequest request, String cookieNameToken) {
		Cookie[] cookies = request.getCookies();
		for(Cookie cookie: cookies){
			if(cookie.getName().equals(cookieNameToken)){
				return cookie.getValue();
			}
		}

		return null;
	}


}
