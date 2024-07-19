package com.rainlf.weixin.v1.domain.consts;

import lombok.Getter;

/**
 * @author rain
 * @date 6/17/2024 2:36 PM
 */
@Getter
public enum MahjongFanEnum {
    MJ_DOOR_CLEAN_FAN("门清"),
    MJ_PENG_PENG_FAN("碰碰胡"),
    MJ_SAME_COLOR_FAN("清一色"),
    MJ_BIG_CRANE_FAN("大吊车"),
    MJ_SEVEN_PAIR_FAN("七小对"),
    MJ_FLOWER_OPEN_FAN("杠开"),
    ;

    private final String name;

    MahjongFanEnum(String name) {
        this.name = name;
    }
}
