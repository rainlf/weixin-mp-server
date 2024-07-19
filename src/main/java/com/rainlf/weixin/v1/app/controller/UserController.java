package com.rainlf.weixin.v1.app.controller;


import com.rainlf.weixin.v1.app.dto.ApiResp;
import com.rainlf.weixin.v1.app.dto.UserInfoDto;
import com.rainlf.weixin.v1.domain.service.UserService;
import com.rainlf.weixin.v1.infra.db.entity.User;
import com.rainlf.weixin.v1.infra.db.repository.UserRepository;
import com.rainlf.weixin.v1.infra.sso.SsoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.List;

/**
 * @author rain
 * @date 5/30/2024 10:48 AM
 */
@Slf4j
@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    public SsoService ssoService;
    @Autowired
    public UserService userService;
    @Autowired
    private UserRepository userRepo;

    @GetMapping("/current")
    public ApiResp<UserInfoDto> getCurrentUser() {
        User user = ssoService.getCurrentUser();
        log.info("getCurrentUser, user: {}", user);
        return ApiResp.success(userService.getUser(user));
    }

    @PostMapping("/current")
    public ApiResp<UserInfoDto> updateCurrentUser(@RequestParam("nickname") String nickname, @RequestParam("avatar") String avatar) {
        User user = ssoService.getCurrentUser();
        log.info("updateCurrentUser, user: {}, nickname: {}, avatar: {}", user, nickname, avatar);
        return ApiResp.success(userService.updateUser(user, nickname, avatar));
    }

    @GetMapping("/list")
    public ApiResp<List<UserInfoDto>> allInitedUser() {
        return ApiResp.success(userService.getAllInitedUser());
    }

    @PostMapping("/uploadAvatar")
    public ApiResp<Void> uploadFile(@RequestParam("userId") Integer userId, @RequestParam("file") MultipartFile file) throws IOException {
        byte[] bytes = file.getBytes();
        String value = Base64.getEncoder().encodeToString(bytes);
        User user = userRepo.findById(userId).orElseThrow(() -> new RuntimeException("用户不存在"));
        user.setAvatar(value);
        userRepo.save(user);
        return ApiResp.success();
    }
}


