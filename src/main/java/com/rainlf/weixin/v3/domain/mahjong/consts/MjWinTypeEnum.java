package com.rainlf.weixin.v3.domain.mahjong.consts;

import lombok.Getter;

/**
 * @author rain
 * @date 7/20/2024 6:19 AM
 */
@Getter
public enum MjWinTypeEnum {
    PING_HU(1, "平胡"),
    ZI_MO(2, "自摸"),
    ONLINE_MJ(3, "线上麻将"),
    ;

    private final int code;
    private final String name;

    MjWinTypeEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public static MjWinTypeEnum fromCode(Integer code) {
        if (code != null) {
            for (MjWinTypeEnum value : MjWinTypeEnum.values()) {
                if (value.code == code) {
                    return value;
                }
            }
        }
        return null;
    }
}
