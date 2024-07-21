package com.rainlf.weixin.v3.app.dto;

import lombok.Data;

import java.util.List;

/**
 * @author rain
 * @date 7/21/2024 5:00 PM
 */
@Data
public class OnlineGameDTO {
    private Integer recordrId;
    private List<Item> items;

    @Data
    public static class Item {
        private Integer userId;
        private Integer score;
    }

    public boolean isValid() {
        return items != null
                && recordrId != null
                && !items.isEmpty()
                && items.stream().allMatch(item -> item.getUserId() != null)
                && items.stream().allMatch(item -> item.getScore() != null)
                && items.stream().map(Item::getUserId).distinct().count() == items.size()
                && items.stream().mapToInt(Item::getScore).sum() == 0;
    }
}
