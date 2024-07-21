package com.rainlf.weixin.v3.domain.mahjong.consts;

import lombok.Getter;

/**
 * @author rain
 * @date 7/20/2024 6:27 AM
 */
@Getter
public enum MjPointOperatorEnum {
    WU_HUA_GUO(1, "无花果", OperatorType.ADD, 10),
    MEN_QIAN_QING(2, "门清", OperatorType.MUL, 2),
    PENG_PENG_HU(3, "碰碰胡", OperatorType.MUL, 2),
    JIANG_JIANG_HU(4, "将将胡", OperatorType.MUL, 2),
    HUN_YI_SE(5, "混一色", OperatorType.MUL, 2),
    QING_YI_SE(6, "清一色", OperatorType.MUL, 4),
    QI_XIAO_DUI(7, "七小对", OperatorType.MUL, 4),
    LONG_QI_DUI(8, "龙七对", OperatorType.MUL, 8),
    DA_DIAO_CHE(9, "大吊车", OperatorType.MUL, 2),
    GANG_KAI(10, "杠开", OperatorType.MUL, 2),
    KAI_BAO(11, "开宝", OperatorType.MUL, 2),
    ;

    private final int code;
    private final String name;
    private final OperatorType operatorType;
    private final int operatorValue;

    MjPointOperatorEnum(int code, String name, OperatorType operatorType, int operatorValue) {
        this.code = code;
        this.name = name;
        this.operatorType = operatorType;
        this.operatorValue = operatorValue;
    }

    public static MjPointOperatorEnum fromCode(int code) {
        for (MjPointOperatorEnum value : MjPointOperatorEnum.values()) {
            if (value.code == code) {
                return value;
            }
        }
        return null;
    }

    @Getter
    public enum OperatorType {
        ADD(1, "加分"),
        MUL(2, "乘分"),
        ;

        private final int code;
        private final String name;

        OperatorType(int code, String name) {
            this.code = code;
            this.name = name;
        }
    }
}

