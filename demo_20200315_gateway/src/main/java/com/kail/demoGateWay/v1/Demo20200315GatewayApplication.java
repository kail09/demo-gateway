package com.kail.demoGateWay.v1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;

import com.kail.demoGateWay.v1.zuulFilter.ErrorFilter;
import com.kail.demoGateWay.v1.zuulFilter.PostFilter;
import com.kail.demoGateWay.v1.zuulFilter.PreFilter;
import com.kail.demoGateWay.v1.zuulFilter.RouteFilter;

@SpringBootApplication
@EnableZuulProxy
public class Demo20200315GatewayApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(Demo20200315GatewayApplication.class, args);
	}
	
	@Bean
	public PreFilter preFilter() {
		return new PreFilter();
	}
	
	@Bean
	public PostFilter postFilter() {
		return new PostFilter();
	}
	
	@Bean
	public ErrorFilter errorFilter() {
		return new ErrorFilter();
	}
	
	@Bean
	public RouteFilter routeFilter() {
		return new RouteFilter();
	}
}
