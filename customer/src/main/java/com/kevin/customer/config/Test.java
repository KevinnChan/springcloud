package com.kevin.customer.config;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Created by Kevin on 2019/2/25.
 */
public class Test {
	public static void main(String[] args) {
		String pwd = "admin";
		String pwd1 = "admin";

		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		String pwdEncoder = encoder.encode(pwd);
		System.out.println(pwdEncoder);
		System.out.println(encoder.matches(pwd1,pwdEncoder));

	}
}
