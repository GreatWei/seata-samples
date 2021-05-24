package io.seata.sample.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author jimin.jm@alibaba-inc.com
 * @date 2019/06/14
 */
@Service
public class AccountService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void reduce(String userId, int money) {
        jdbcTemplate.update("update account_tbl set money = money - ? where user_id = ?", new Object[] {money, userId});
    }

    public Boolean enoughMoney(String userId,int cost){
        Map accountMap = jdbcTemplate.queryForMap("select * from account_tbl where user_id=?",new Object[]{userId});
        int money = Integer.parseInt(accountMap.get("money").toString());
        return money>cost;
    }
}
