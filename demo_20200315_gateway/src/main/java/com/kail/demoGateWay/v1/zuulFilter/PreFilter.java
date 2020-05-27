package com.kail.demoGateWay.v1.zuulFilter;

import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.tomcat.util.json.JSONParser;
import org.apache.tomcat.util.json.ParseException;
import org.json.simple.JSONObject;
import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
		
		log.info("Request Method : " + request.getMethod());
		log.info("Request URL : " + request.getRequestURL().toString());
		log.info("Request URI : " + request.getRequestURI().toString());
		
		String strArr[] = (request.getRequestURI().toString()).split("/");
		System.out.println("chk : "+Arrays.toString(strArr));
		
		
		
		HttpSession session = request.getSession(true);
		boolean vaildPass = true;
		/*
        // ui 서버 호출일 경우
        if ("ui".equals(strArr[1])) {
            if ( "login_Admin".equals(strArr[2]) 
                    || "js".equals(strArr[2])
                    || "img".equals(strArr[2])
            ) {
                vaildPass = true;
            }
        } 
        // api 서버 호출일 경우
        else if ( "api".equals(strArr[1]) ) {
            if ( "member".equals(strArr[2]) && "login".equals(strArr[3]) ) {
                vaildPass = true;
            }
        }*/
		
		String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
		if (!vaildPass) {
			if ( !validateToken(authorizationHeader) ) {
				ctx.setSendZuulResponse(false);
				ctx.setResponseBody("API key not authorized");
				ctx.getResponse().setHeader("Content-Type", "text/plain;charset=UTF-8");
				ctx.setResponseStatusCode(HttpStatus.UNAUTHORIZED.value());
			}
		}
		
		return null;
	}
	
	private boolean validateToken(String tokenHeader) {
		// do something to validate the token
		
		if (StringUtils.isBlank(tokenHeader)) {
			return false;
		}
		
		return true;
	}
	
	private boolean validateData(HttpSession data) {
		boolean result =  true;

		ObjectMapper mapper = new ObjectMapper();
		String chk = (String) data.getAttribute("member");
		System.out.println("chk : "+ chk);
		if (chk != null) {
		
		
		String jsonStr = null;
		try {
			jsonStr = mapper.writeValueAsString((String) data.getAttribute("member"));
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			log.info("parser mapper Error");
			e.printStackTrace();
		}
		JSONParser parser = new JSONParser(jsonStr);
		Object obj = null;
		try {
			obj = parser.parse();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			log.info("parser Error");
			e.printStackTrace();
		}
		  
		JSONObject jsonObj = (JSONObject)obj;
		
		System.out.println("jsonObj : "+ jsonObj.get("userName"));

		}
		
		return result;
	}
	
}
