package com.rainlf.weixin.v3.domain.mahjong.consts;

import lombok.Getter;

/**
 * @author rain
 * @date 7/21/2024 9:49 AM
 */
@Getter
public enum MjUserTypeEnum {
    PLAYER(1),
    RECODER(2),
    ;

    private final int code;

    MjUserTypeEnum(int code) {
        this.code = code;
    }

    public static MjUserTypeEnum fromCode(Integer code) {
        if (code != null) {
            for (MjUserTypeEnum value : MjUserTypeEnum.values()) {
                if (value.code == code) {
                    return value;
                }
            }
        }
        return null;
    }
}
