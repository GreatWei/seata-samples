package io.seata.sample.service;

import java.util.Map;

import javax.annotation.PostConstruct;

import io.seata.sample.feign.AccountClient;
import io.seata.sample.feign.OrderFeignClient;
import io.seata.sample.feign.StorageFeignClient;
import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

/**
 * @author jimin.jm@alibaba-inc.com
 * @date 2019/06/14
 */
@Service
public class BusinessService {

    @Autowired
    private StorageFeignClient storageFeignClient;
    @Autowired
    private OrderFeignClient orderFeignClient;
    @Autowired
    private AccountClient accountClient;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 减库存，下订单
     *
     * @param userId
     * @param commodityCode
     * @param orderCount
     */
    @GlobalTransactional
    public void purchase(String userId, String commodityCode, int orderCount) {
        storageFeignClient.deduct(commodityCode, orderCount);

        orderFeignClient.create(userId, commodityCode, orderCount);
        if (!accountClient.enoughMoney(userId,orderCount*100)){
            throw new RuntimeException("账户不足,执行回滚");
        }
        if (!storageFeignClient.enoughStorage(commodityCode,orderCount)){
            throw new RuntimeException("库存不足,执行回滚");
        }
    }

//    @PostConstruct
//    public void initData() {
//        jdbcTemplate.update("delete from account_tbl");
//        jdbcTemplate.update("delete from order_tbl");
//        jdbcTemplate.update("delete from storage_tbl");
//        jdbcTemplate.update("insert into account_tbl(user_id,money) values('U100000','10000') ");
//        jdbcTemplate.update("insert into storage_tbl(commodity_code,count) values('C100000','200') ");
//    }
}
