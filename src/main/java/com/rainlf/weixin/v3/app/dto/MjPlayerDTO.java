package com.rainlf.weixin.v3.app.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author rain
 * @date 7/21/2024 1:43 PM
 */
@Data
public class MjPlayerDTO {
    private Integer userId;
    private String userNickname;
    private LocalDateTime lastGameTime;

    public MjPlayerDTO(Integer userId, String userNickname, LocalDateTime lastGameTime) {
        this.userId = userId;
        this.userNickname = userNickname;
        this.lastGameTime = lastGameTime;
    }
}
