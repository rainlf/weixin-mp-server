package com.rainlf.weixin.v3.domain.mahjong.model;

import lombok.Data;

import java.util.List;

/**
 * @author rain
 * @date 7/21/2024 3:19 PM
 */
@Data
public class MjGameLog {
    private String gameId;
    private List<MjUserLog> mjLogs;

    public MjGameLog(String gameId, List<MjUserLog> mjLogs) {
        this.gameId = gameId;
        this.mjLogs = mjLogs;
    }
}
