package com.rainlf.weixin.app.controller;


import com.rainlf.weixin.app.dto.ApiResp;
import com.rainlf.weixin.app.dto.UserInfoDto;
import com.rainlf.weixin.domain.service.UserService;
import com.rainlf.weixin.infra.db.entity.User;
import com.rainlf.weixin.infra.sso.SsoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
}
