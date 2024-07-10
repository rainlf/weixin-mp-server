package com.rainlf.weixin.app.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author rain
 * @date 6/14/2024 8:23 PM
 */
@Data
public class MahjongLogDto {
    private Integer gameId;
    private Integer recorderId;
    private String recorderName;
    private String recorderAvatar;
    private Integer recorderAward;
    private List<String> gameTags;
    private List<Item> winners;
    private List<Item> losers;
    private LocalDateTime createTime;

    @Data
    public static class Item {
        private Integer userId;
        private String userName;
        private String userAvatar;
        private Integer score;
    }
}
