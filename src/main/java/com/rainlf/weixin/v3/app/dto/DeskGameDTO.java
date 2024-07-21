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
    private List<Item> items;

    @Data
    public static class Item {
        private Integer userId;
        private Integer point;
        private List<MjPointOperatorEnum> pointOperators;
        private Integer score;
    }

    public boolean isValid() {
        return items != null
                && recordrId != null
                && gameType != null
                && !items.isEmpty()
                && items.stream().allMatch(item -> item.getUserId() != null)
                && items.stream().allMatch(item -> item.getScore() != null);
    }
}
