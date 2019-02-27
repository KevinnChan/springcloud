package com.kevin.login.service.feign;

import com.kevin.customer.service.AccountService;
import org.springframework.cloud.netflix.feign.FeignClient;

/**
 * Created by Kevin on 2018/12/13.
 */
@FeignClient(name = "customer")
public interface AccountServiceProxy extends AccountService {
}
