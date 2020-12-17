package com.haoboliu.service;

import com.haoboliu.bean.Account;
import com.haoboliu.mapper.AccountMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountService {

    @Autowired
    AccountMapper accountMapper;

    // slf4j
    private static final Logger logger = LoggerFactory.getLogger(AccountService.class);

    public Account selectByUsernameAndPassword(String username, String password) {
        return accountMapper.selectByUsernameAndPassword(username, password);
    }

    public Account selectByUsernameAndEmail(String username, String email) {
        return accountMapper.selectByUsernameAndEmail(username, email);
    }

    public Integer insertAccount(Account account) {
        return accountMapper.insert(account);
    }

    public String getPassword(String username) {
       return accountMapper.selectByUsernameAndEmail(username, null).getPassword();
    }

    public Account selectByUserId(Integer id) {
        return accountMapper.selectByPrimaryKey(id);
    }
}
