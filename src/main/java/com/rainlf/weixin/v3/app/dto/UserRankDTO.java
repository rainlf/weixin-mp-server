package com.rainlf.weixin.v3.app.dto;

import lombok.Data;

import java.util.List;

/**
 * @author rain
 * @date 7/20/2024 8:52 AM
 */
@Data
public class UserRankDTO {
    private Integer topUserId;
    private String topUserNickname;
    private Integer topUserCoin;
    private Integer bottomUserId;
    private String bottomUserNickname;
    private Integer bottomUserCoin;
    private List<RankItem> rankItems;

    public static class RankItem {
        private Integer userId;
        private String userNickname;
        private Integer userCoin;
        private List<String> userTags;
    }
}
