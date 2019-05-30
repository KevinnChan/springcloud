package com.kevin.gateway.rest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Kevin on 2019/5/30.
 */
@RestController
@RequestMapping("/test")
public class TestController {

	@RequestMapping("/hello")
	public String sayHello() {
		return "Hello Zuul";
	}
}
