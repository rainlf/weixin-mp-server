package com.rainlf.weixin.v3.app.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author rain
 * @date 7/20/2024 8:54 AM
 */
@Data
public class MjLogDTO {
    private String gameId;
    private boolean canDelete;
    private String gameType;
    private List<Player> winners;
    private List<Player> losers;
    private Player recordUser;
    private LocalDateTime createTime;

    @Data
    public static class Player {
        private Integer userId;
        private String userNickname;
        private Integer userScore;
        private List<String> userTags;

        public Player(Integer userId, String userNickname, Integer userScore) {
            this.userId = userId;
            this.userNickname = userNickname;
            this.userScore = userScore;
            this.userTags = new ArrayList<>();
        }

        public Player(Integer userId, String userNickname, Integer userScore, List<String> userTags) {
            this.userId = userId;
            this.userNickname = userNickname;
            this.userScore = userScore;
            this.userTags = userTags;
        }
    }


}
