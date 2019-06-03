package com.kevin.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by Kevin on 2019/6/3.
 */
@Configuration
public class BeanConfiguration {
	@Bean
	public DynamicFilterFileManager dynamicFilterFileManager() {
		DynamicFilterFileManager dynamicFilterFileManager = DynamicFilterFileManager.getInstance();
		dynamicFilterFileManager.init(5);
		return dynamicFilterFileManager;
	}
}
