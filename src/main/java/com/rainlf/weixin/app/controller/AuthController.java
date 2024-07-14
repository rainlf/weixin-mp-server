package com.rainlf.weixin.app.controller;

import com.rainlf.weixin.app.dto.ApiResp;
import com.rainlf.weixin.domain.service.AuthService;
import com.rainlf.weixin.infra.db.entity.User;
import com.rainlf.weixin.infra.db.repository.UserRepository;
import com.rainlf.weixin.infra.util.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Base64;

/**
 * @author rain
 * @date 6/14/2024 11:21 AM
 */
@Slf4j
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private AuthService authService;
    @Autowired
    private UserRepository userRepo;

    @GetMapping("/test")
    public ApiResp<String> test() {
        log.info("test, ok");
        return ApiResp.success("ok");
    }

    @PostMapping("/login")
    public ApiResp<String> login(@RequestParam("code") String code) {
        log.info("login code:{}", code);
        return ApiResp.success(authService.login(code));
    }

    @PostMapping("/mockLogin")
    public ApiResp<String> mockLogin() {
        return ApiResp.success(authService.mockLogin());
    }

    @GetMapping("/verifyToken")
    public ApiResp<String> verifyToken(@RequestParam("token") String token) {
        JwtUtils.getOpenId(token);
        return ApiResp.success();
    }
}
