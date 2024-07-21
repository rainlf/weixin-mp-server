package com.rainlf.weixin.v3.app.controller;

import com.rainlf.weixin.v3.app.dto.UserDTO;
import com.rainlf.weixin.v3.app.dto.ApiResp;
import com.rainlf.weixin.v3.app.assembler.UserDTOAssembler;
import com.rainlf.weixin.v3.domain.user.UserService;
import com.rainlf.weixin.v3.infa.auth.AuthService;
import com.rainlf.weixin.v3.infa.db.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author rain
 * @date 7/20/2024 6:51 AM
 */
@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private AuthService authService;
    @Autowired
    private UserService userService;
    @Autowired
    private UserDTOAssembler userDTOAssembler;

    @PostMapping("/login")
    public ApiResp<String> login(@RequestParam("code") String code) {
        log.info("login code:{}", code);
        return ApiResp.success(userService.login(code));
    }

    @GetMapping("/info")
    public ApiResp<UserDTO> getCurrentUser() {
        User user = authService.getUser();
        UserDTO userDTO = userDTOAssembler.fromUserDO(user);
        return ApiResp.success(userDTO);
    }

    @PostMapping("/info")
    public ApiResp<Void> updateCurrentUser(@RequestParam("nickname") String nickname, @RequestParam("file") MultipartFile file) {
        User user = authService.getUser();
        userService.updateUser(user, nickname, file);
        return ApiResp.success();
    }

    @GetMapping("/avatar")
    public ResponseEntity<byte[]> getCurrentUserAvatar() {
        User user = authService.getUser();
        byte[] avatar = user.getAvatar();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        return new ResponseEntity<>(avatar, headers, HttpStatus.OK);
    }
}
