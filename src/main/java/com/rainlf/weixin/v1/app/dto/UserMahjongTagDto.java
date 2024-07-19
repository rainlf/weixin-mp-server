package com.rainlf.weixin.v1.app.dto;

import lombok.Data;

import java.util.List;

/**
 * @author rain
 * @date 6/23/2024 12:51 PM
 */
@Data
public class UserMahjongTagDto {
    private Integer userId;
    private List<String> tags;
}
