package com.kevin.customer.service;

import com.kevin.common.dto.ResultBaseDTO;
import com.kevin.customer.config.CustomerConfig;
import com.kevin.customer.dto.AccountDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Kevin on 2018/12/11.
 */
@Slf4j
@RestController
public class AccountServiceImpl implements AccountService {

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public ResultBaseDTO<Long> register(@RequestBody AccountDTO accountDTO) {
		return null;
	}

	@Override
	public ResultBaseDTO<AccountDTO> verify(@RequestBody AccountDTO accountDTO) {
		if (!CustomerConfig.DATASOURCE.containsKey(accountDTO.getLoginName())) {
			return ResultBaseDTO.failure("Invalid loginName or password!");
		}

		log.info("account verify input param {}", accountDTO);

		if (!CustomerConfig.DATASOURCE.get(accountDTO.getLoginName()).getPassword().equals(accountDTO.getPassword())) {
			return ResultBaseDTO.failure("Invalid loginName or password!");
		}


		return ResultBaseDTO.success(CustomerConfig.DATASOURCE.get(accountDTO.getLoginName()));

	}
}
