package com.taxi.project.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class UserInterceptor extends HandlerInterceptorAdapter{

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
			
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		
		if (logger.isDebugEnabled()) {
			logger.debug(" @@ preHandle Point ==> ");
			logger.debug(" @@ Request URI \t: " + request.getRequestURI());
		}
		
		return true;
	}
	
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		
		if (logger.isDebugEnabled()) {
			logger.debug(" @@ postHandle Point ==> ");
		}
		
	}
	
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		logger.info("@@ afterCompletion Point ==> ");
	}
}
