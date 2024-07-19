package com.rainlf.weixin.v3.domain.mahjong;

import lombok.Getter;

/**
 * @author rain
 * @date 7/20/2024 6:19 AM
 */
@Getter
public enum MjWinTypeEunm {
    PING_HU(1, "平胡"),
    ZI_MO(2, "自摸"),
    ;

    private final int code;
    private final String name;

    MjWinTypeEunm(int code, String name) {
        this.code = code;
        this.name = name;
    }
}
