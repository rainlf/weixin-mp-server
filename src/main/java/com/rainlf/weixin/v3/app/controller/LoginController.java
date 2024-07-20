package com.rainlf.weixin.v3.app.controller;

import com.rainlf.weixin.v3.app.dto.base.ApiResp;
import com.rainlf.weixin.v3.infa.auth.AuthService;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author rain
 * @date 7/20/2024 6:45 AM
 */
@Slf4j
@RestController
@RequestMapping("/api/login")
public class LoginController {

    @Autowired
    private AuthService authService;

    // rain
    @PostMapping("")
    public ApiResp<String> login(@RequestParam("code") String code) {
        log.info("login code:{}", code);
        return ApiResp.success(authService.createToken(code));
    }
}
