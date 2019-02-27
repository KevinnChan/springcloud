package com.kevin.login.rest;

import com.kevin.common.dto.ResultBaseDTO;
import com.kevin.customer.dto.AccountDTO;
import com.kevin.login.config.JWTConfig;
import com.kevin.login.service.feign.AccountServiceProxy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Kevin on 2018/12/13.
 */
@Slf4j
@RestController
@RequestMapping(value = "/login")
public class LoginRestController {
	@Autowired
	private AccountServiceProxy accountService;

	@RequestMapping(value = "/byPassword", method = RequestMethod.POST)
	public ResultBaseDTO<String> loginByPassword(@RequestBody AccountDTO accountDTO) {
		try {
			ResultBaseDTO<AccountDTO> rs = accountService.verify(accountDTO);
			if (rs.getIsSuccess()) {
				return ResultBaseDTO.success(JWTConfig.createToken(rs.getData()));
			}

			return ResultBaseDTO.failure(rs.getErrorMassage());
		} catch (Exception e) {
			log.error("login exception", e);
			return ResultBaseDTO.failure("系统异常");
		}
	}
}
