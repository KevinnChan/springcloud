package com.kevin;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

@Slf4j
@EnableZuulProxy
@SpringBootApplication
public class GatewayApplication {

/*	@Bean
	public InputParamLogFilter inputParamLogFilter(){
		log.info("init InputParamLogFilter");
		return new InputParamLogFilter();
	}*/

	public static void main(String[] args) {
		SpringApplication.run(GatewayApplication.class, args);
	}
}
