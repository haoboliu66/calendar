package com.haoboliu.service;

import com.haoboliu.bean.Account;
import com.haoboliu.mapper.AccountMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountService {

    @Autowired
    AccountMapper accountMapper;

    public Account selectByUsernameAndPassword(String username, String password) {
        return accountMapper.selectByUsernameAndPassword(username, password);
    }

    public Account selectByUsername(String username) {
        return accountMapper.selectByUsername(username);
    }

    public Integer insertAccount(Account account) {
        return accountMapper.insert(account);
    }

    public String getPassword(String username) {
       return accountMapper.selectByUsername(username).getPassword();
    }
}
