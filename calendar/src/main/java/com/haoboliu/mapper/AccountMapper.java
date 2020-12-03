package com.haoboliu.mapper;

import com.haoboliu.bean.Account;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

/**
 * AccountMapper继承基类
 */
@Repository
public interface AccountMapper extends MyBatisBaseDao<Account, Integer> {

    Account selectByUsernameAndPassword(String username, String password);

    Account selectByUsername(String username);

}