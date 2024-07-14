package com.rainlf.weixin.app.dto;

import lombok.Data;

/**
 * @author rain
 * @date 6/14/2024 6:54 PM
 */
@Data
public class UserInfoDto {
    private Integer id;
    private String nickname;
//    private String avatar;
    private Integer copperCoin;
    private Integer silverCoin;
    private Integer goldCoin;
}
