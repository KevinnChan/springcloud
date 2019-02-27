package com.kevin.customer.config;

import com.kevin.customer.dto.AccountDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Kevin on 2018/12/11.
 */
@Slf4j
@Component
public class CustomerConfig {
	public static volatile Map<String, AccountDTO> DATASOURCE = new ConcurrentHashMap<>();

	@PostConstruct
	public void init() {
		AccountDTO admin = new AccountDTO();
		admin.setId(1L);
		admin.setLoginName("admin");
		admin.setPassword("admin123");

		AccountDTO user = new AccountDTO();
		user.setId(2L);
		user.setLoginName("user");
		user.setPassword("user123");
		DATASOURCE.put(admin.getLoginName(), admin);
		DATASOURCE.put(user.getLoginName(), user);

		log.info("Customer init finished.");
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
