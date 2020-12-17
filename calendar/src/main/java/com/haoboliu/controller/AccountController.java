package com.haoboliu.controller;

import com.alibaba.fastjson.JSONObject;
import com.haoboliu.bean.Account;
import com.haoboliu.returnjson.ReturnObject;
import com.haoboliu.service.AccountService;
import com.haoboliu.service.EventService;
import com.haoboliu.utils.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/account")
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {}, allowCredentials = "true")
public class AccountController {

    @Autowired
    AccountService accountService;


    // slf4j
    private static final Logger logger = LoggerFactory.getLogger(AccountController.class);

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String login(@RequestParam("username") String username, @RequestParam("password") String password, HttpSession session) {
        logger.info("Username: " + username);
        logger.info("Password: " + password);
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
        logger.info("Validate: " + username);
        Account account = accountService.selectByUsernameAndEmail(username, null);
        ReturnObject returnObject = new ReturnObject();
        if (account != null) {
            returnObject = new ReturnObject().setCode(-1).setMessage("userName exists");
        }
        return JSONObject.toJSONString(returnObject);
    }


    @PostMapping("/register")
    public String register(String username, String password, String email) {
        logger.info("Register Username: " + username);
        logger.info("Register Password: " + password);
        logger.info("Register Email: " + email);
        Account validate = accountService.selectByUsernameAndEmail(username, email);
        if(validate != null){
            logger.warn("Email exists");
            return JSONObject.toJSONString(new ReturnObject(null).setCode(-1));
        }
        Account account = new Account(username, password, email);
        Integer res = accountService.insertAccount(account);
        ReturnObject returnObject = new ReturnObject();
        return res > 0? JSONObject.toJSONString(returnObject): JSONObject.toJSONString(returnObject.setCode(-1));
    }

    @GetMapping("/logout")
    public void logOut(HttpSession session) {
        logger.info("log out");
        session.invalidate();
    }


}
