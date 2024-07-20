package com.rainlf.weixin.v3.app.dto;

import lombok.Data;

/**
 * @author rain
 * @date 7/20/2024 8:54 AM
 */
@Data
public class MjLogDTO {
    private String gameId;

    public static class Player {
        private Integer userId;
        private String userNickname;
        private Integer userCoin;
        private Integer userScore;
    }
}
