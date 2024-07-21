package com.rainlf.weixin.v3.app.dto;

import com.rainlf.weixin.v3.domain.mahjong.model.MjUserLog;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author rain
 * @date 7/20/2024 8:54 AM
 */
@Data
public class MjGameLogDTO {
    private String gameId;
    private String gameType;
    private boolean canDelete;
    private LocalDateTime createTime;
    private Player recordUser;
    private List<Player> winners;
    private List<Player> losers;

    @Data
    public static class Player {
        private Integer userId;
        private String userNickname;
        private Integer userScore;
        private List<String> userTags;

        public Player(MjUserLog mjUserLog) {
            this.userId = mjUserLog.getUser().getId();
            this.userNickname = mjUserLog.getUser().getNickname();
            this.userScore = mjUserLog.getMjLog().getScore();
            this.userTags = mjUserLog.getMjLog().getTags();
        }
    }


}
