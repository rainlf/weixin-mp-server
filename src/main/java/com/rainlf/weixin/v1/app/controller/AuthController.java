package com.rainlf.weixin.v1.app.controller;

import com.rainlf.weixin.v1.app.dto.ApiResp;
import com.rainlf.weixin.v1.domain.service.AuthService;
import com.rainlf.weixin.v1.infra.db.entity.User;
import com.rainlf.weixin.v1.infra.db.repository.UserRepository;
import com.rainlf.weixin.v1.infra.util.JwtUtils;
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

    @GetMapping("/getAvatar")
    public ResponseEntity<byte[]> getImage(@RequestParam("userId") Integer userId) {
        User user = userRepo.findById(userId).orElseThrow(() -> new RuntimeException("用户不存在"));
        String value = user.getAvatar();
        byte[] bytes = Base64.getDecoder().decode(value);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        return new ResponseEntity<>(bytes, headers, HttpStatus.OK);
    }
}
