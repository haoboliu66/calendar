package com.interceptor;

import com.haoboliu.service.AccountService;
import com.haoboliu.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class JwtInterceptor implements HandlerInterceptor {

    @Autowired
    AccountService accountService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // get token from http header
        String token = request.getHeader("token");
        System.out.println("token: " + token);
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        if (token != null) {
            String username = JwtUtil.getUserNameByToken(request);
            System.out.println("username == " + username);
            // get password according to username and verify
            boolean result = JwtUtil.verify(token, username, accountService.getPassword(username));
            System.out.println("result: " + result);
            if (result) {
                System.out.println("pass interceptor");
                return true;
            }
        }
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
