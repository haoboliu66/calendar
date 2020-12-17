package com.haoboliu.controller;

import com.haoboliu.service.AccountService;
import me.zhyd.oauth.config.AuthConfig;
import me.zhyd.oauth.model.AuthCallback;
import me.zhyd.oauth.request.AuthGoogleRequest;
import me.zhyd.oauth.request.AuthRequest;
import me.zhyd.oauth.utils.AuthStateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {}, allowCredentials = "true")
public class AuthController {

    private static final String clientId = "995665339175-kvr7eb3slh81dbr9vlkh9rshu4p2el12.apps.googleusercontent.com";
    private static final String clientSecret = "TmKrwOhd_s_VztFz3Age4Rs1";
    private static final String callBack = "http://localhost:9999/oauth/callback/google";
    //http://localhost:8443/oauth/callback/{平台名}
    //Client ID: 995665339175-kvr7eb3slh81dbr9vlkh9rshu4p2el12.apps.googleusercontent.com
    //Client secret: TmKrwOhd_s_VztFz3Age4Rs1

    @Autowired
    AccountService accountService;

    @RequestMapping("/oauth")
    public String auth(String accessToken){
        System.out.println(accessToken);
        return "hello token";
    }

    @RequestMapping("/render")
    public void renderAuth(HttpServletResponse response) throws IOException {
        AuthRequest authRequest = getAuthRequest();
        response.sendRedirect(authRequest.authorize(AuthStateUtils.createState()));
    }

    @RequestMapping("/callback")
    public Object login(AuthCallback callback) {
        AuthRequest authRequest = getAuthRequest();
        return authRequest.login(callback);
    }

    private AuthRequest getAuthRequest() {
        return new AuthGoogleRequest(AuthConfig.builder()
                .clientId(clientId)
                .clientSecret(clientSecret)
                .redirectUri(callBack)
                .build());
    }


}
