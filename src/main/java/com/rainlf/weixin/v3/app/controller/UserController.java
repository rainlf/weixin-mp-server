package com.rainlf.weixin.v3.app.controller;

import com.rainlf.weixin.v3.app.dto.TopUsersDTO;
import com.rainlf.weixin.v3.app.dto.UserDTO;
import com.rainlf.weixin.v3.app.dto.base.ApiResp;
import com.rainlf.weixin.v3.app.dto.mapper.UserDTOMapper;
import com.rainlf.weixin.v3.domain.mahjong.UserSevice;
import com.rainlf.weixin.v3.infa.auth.AuthService;
import com.rainlf.weixin.v3.infa.db.entity.UserDO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author rain
 * @date 7/20/2024 6:51 AM
 */
@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserSevice userSevice;
    @Autowired
    private AuthService authService;
    @Autowired
    private UserDTOMapper userDTOMapper;

    @GetMapping("/current")
    public ApiResp<UserDTO> getCurrentUser() {
        UserDO userDO = authService.getUser();
        UserDTO userDTO = userDTOMapper.fromUserDO(userDO);
        return ApiResp.success(userDTO);
    }

    @GetMapping("/top")
    public ApiResp<TopUsersDTO> getTopUsers() {
        TopUsersDTO topUsersDTO = userSevice.getTopUsers();
        return ApiResp.success(topUsersDTO);
    }


}
