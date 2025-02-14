package io.seata.sample.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@FeignClient(name = "account-service", url = "127.0.0.1:8083")
public interface AccountClient {
    @GetMapping(value = "/enoughmoney")
    Boolean enoughMoney(@RequestParam("userId")String userId,@RequestParam("cost") Integer cost);
}
