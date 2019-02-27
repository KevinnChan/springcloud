package com.kevin.customer.service;

import com.kevin.common.dto.ResultBaseDTO;
import com.kevin.customer.dto.AccountDTO;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by Kevin on 2018/12/11.
 */
@RequestMapping(value="/v1/account")
public interface AccountService {
	@RequestMapping(method = RequestMethod.POST)
	ResultBaseDTO<Long> register(@RequestBody AccountDTO accountDTO);

	@RequestMapping(value = "/verify",method = RequestMethod.POST)
	ResultBaseDTO<AccountDTO> verify(@RequestBody AccountDTO accountDTO);
}
