package com.rainlf.weixin.v3.app.dto;

import com.rainlf.weixin.v3.domain.mahjong.consts.MjGameTypeEnum;
import com.rainlf.weixin.v3.domain.mahjong.consts.MjPointOperatorEnum;
import lombok.Data;

import java.util.List;

/**
 * @author rain
 * @date 7/21/2024 5:37 PM
 */
@Data
public class DeskGameDTO {
    private Integer recordrId;
    private MjGameTypeEnum gameType;
    private List<Item> winners;
    private List<Item> losers;

    @Data
    public static class Item {
        private Integer userId;
        private Integer point;
        private List<MjPointOperatorEnum> pointOperators;
        private Integer score;
    }

    public boolean isValid() {
        return recordrId != null
                && gameType != null
                && !winners.isEmpty()
                && winners.stream().allMatch(item -> item.getUserId() != null)
                && winners.stream().allMatch(item -> item.getScore() != null)
                && winners.stream().allMatch(item -> item.getScore() > 0)
                && !losers.isEmpty()
                && losers.stream().allMatch(item -> item.getUserId() != null)
                && losers.stream().allMatch(item -> item.getScore() != null)
                && losers.stream().allMatch(item -> item.getScore() < 0)
                && winners.stream().mapToInt(Item::getScore).sum() + losers.stream().mapToInt(Item::getScore).sum() == 0;
    }
}
