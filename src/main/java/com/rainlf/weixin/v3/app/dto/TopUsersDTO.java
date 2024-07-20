package com.rainlf.weixin.v3.app.dto;

import lombok.Data;

/**
 * @author rain
 * @date 7/20/2024 8:46 AM
 */
@Data
public class TopUsersDTO {
    private Integer topUserId;
    private String topUserNickname;
    private Integer topUserCoin;
    private Integer bottomUserId;
    private String bottomUserNickname;
    private Integer bottomUserCoin;
}
