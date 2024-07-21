package com.rainlf.weixin.v3.domain.mahjong.consts;

import lombok.Getter;

/**
 * @author rain
 * @date 7/20/2024 6:19 AM
 */
@Getter
public enum MjGameTypeEnum {
    PING_HU(1, "平胡"),
    ZI_MO(2, "自摸"),
    YI_PAO_SHUANG_XIANG(3, "一炮双响"),
    YI_PAO_SAN_XIANG(4, "一炮三响"),
    ONLINE_MJ(5, "线上麻将"),
    ;

    private final int code;
    private final String name;

    MjGameTypeEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public static MjGameTypeEnum fromCode(Integer code) {
        if (code != null) {
            for (MjGameTypeEnum value : MjGameTypeEnum.values()) {
                if (value.code == code) {
                    return value;
                }
            }
        }
        return null;
    }

    public boolean isHide() {
        return this == PING_HU || this == ONLINE_MJ;
    }
}
