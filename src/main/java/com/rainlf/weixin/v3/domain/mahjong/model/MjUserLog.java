package com.rainlf.weixin.v3.domain.mahjong.model;

import com.rainlf.weixin.v3.domain.mahjong.consts.MjUserTypeEnum;
import com.rainlf.weixin.v3.infa.db.entity.MjLog;
import com.rainlf.weixin.v3.infa.db.entity.User;
import lombok.Data;

import java.util.Objects;

/**
 * @author rain
 * @date 7/21/2024 3:25 PM
 */
@Data
public class MjUserLog {
    private MjLog mjLog;
    private User user;

    public MjUserLog(MjLog mjLog, User user) {
        this.mjLog = mjLog;
        this.user = user;
    }


    public boolean isWinner() {
        return Objects.equals(MjUserTypeEnum.WINNER, mjLog.getUserType());
    }

    public boolean isLoser() {
        return Objects.equals(MjUserTypeEnum.LOSER, mjLog.getUserType());
    }

    public boolean isRecorder() {
        return Objects.equals(MjUserTypeEnum.RECORDER, mjLog.getUserType());
    }
}
