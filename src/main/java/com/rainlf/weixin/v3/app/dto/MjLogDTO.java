package com.rainlf.weixin.v3.app.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author rain
 * @date 7/20/2024 8:54 AM
 */
@Data
public class MjLogDTO {
    private String gameId;
    private boolean canDelete;
    private LocalDateTime createTime;
    private List<Player> winners;
    private List<Player> losers;
    private Player recordUser;

    public static class Player {
        private String winType;
        private Integer userId;
        private String userNickname;
        private Integer userCoin;
        private List<String> userTags;
    }
}
