package com.haoboliu.mapper;

import com.haoboliu.bean.Account;
import org.springframework.stereotype.Repository;


@Repository
public interface AccountMapper extends MyBatisBaseDao<Account, Integer> {

    Account selectByUsernameAndPassword(String username, String password);

    Account selectByUsernameAndEmail(String username, String email);

}