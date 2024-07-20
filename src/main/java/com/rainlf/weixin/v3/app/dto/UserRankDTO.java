package com.rainlf.weixin.v3.app.dto;

import lombok.Data;

import java.util.List;

/**
 * @author rain
 * @date 7/20/2024 8:52 AM
 */
@Data
public class UserRankDTO {
    private Integer userId;
    private String userNickname;
    private Integer userCoin;
    private List<String> userTags;
}
