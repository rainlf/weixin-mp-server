package com.rainlf.weixin.v3.app.dto;

import com.rainlf.weixin.v3.domain.mahjong.model.MjPlayer;
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

    public MjPlayerDTO(MjPlayer mjPlayer) {
        this.userId = mjPlayer.getUser().getId();
        this.userNickname = mjPlayer.getUser().getNickname();
        this.lastGameTime = mjPlayer.getLastGameTime();
    }
}
