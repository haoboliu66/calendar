package com.haoboliu.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.haoboliu.bean.Account;
import com.haoboliu.returnjson.ReturnObject;
import com.haoboliu.service.AccountService;
import com.haoboliu.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/account")
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {}, allowCredentials = "true")
public class AccountController {

    @Autowired
    AccountService accountService;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String login(@RequestParam("username") String username, @RequestParam("password") String password, HttpSession session) {

        Account account = accountService.selectByUsernameAndPassword(username, password);
        ReturnObject returnObject = new ReturnObject(account);
        if (account == null) {
            returnObject = new ReturnObject().setCode(404).setMessage("No such user");
            return JSONObject.toJSONString(returnObject);
        }
        // add token to success login
        String token = JwtUtil.sign(username,password);
        returnObject.setToken(token);
        return JSONObject.toJSONString(returnObject);
    }

    @RequestMapping("/validateUsername")
    public String validate(@RequestParam("username") String username) {
        System.out.println("validate: " + username);
        Account account = accountService.selectByUsername(username);
        ReturnObject returnObject = new ReturnObject();
        if (account != null) {
            returnObject = new ReturnObject().setCode(-1).setMessage("userName exists");
        }
        return JSONObject.toJSONString(returnObject);
    }


    @PostMapping("/register")
    public String register(String username, String password, String email) {
        System.out.println(username);
        System.out.println(password);
        System.out.println(email);
        Account account = new Account(username, password, email);
        Integer res = accountService.insertAccount(account);
        ReturnObject returnObject = new ReturnObject();
        return res > 0? JSONObject.toJSONString(returnObject): JSONObject.toJSONString(returnObject.setCode(-1));
    }

    @GetMapping("/logout")
    public void logOut(HttpSession session) {
        System.out.println("log out");
        session.invalidate();
    }


}
