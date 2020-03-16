package com.kail.demoGateWay.v1.zuulFilter;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;

import com.google.common.net.HttpHeaders;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PreFilter extends ZuulFilter {
	
	@Override
	public String filterType() {
		return "pre";
	}
	
	@Override
	public int filterOrder() {
		return 1;
	}
	
	@Override
	public boolean shouldFilter() {
		return true;
	}
	
	@Override
	public Object run() {
		RequestContext ctx = RequestContext.getCurrentContext();
		HttpServletRequest request = ctx.getRequest();
		
		log.info("Request Method : " + request.getMethod());;
		log.info("Request URL : " + request.getRequestURL().toString());;
		
		String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
		if ( !validateToken(authorizationHeader))  {
			ctx.setSendZuulResponse(false);
			ctx.setResponseBody("API key not authorized");
			ctx.getResponse().setHeader("Content-Type", "text/plain;charset=UTF-8");
			ctx.setResponseStatusCode(HttpStatus.UNAUTHORIZED.value());
		}
		return null;
	}
	
	private boolean validateToken(String tokenHeader) {
		// do something to validate the token
		return true;
	}
	
}
