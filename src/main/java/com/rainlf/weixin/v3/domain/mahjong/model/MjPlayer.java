package com.rainlf.weixin.v3.domain.mahjong.model;

import com.rainlf.weixin.v3.infa.db.entity.MjLog;
import com.rainlf.weixin.v3.infa.db.entity.User;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/**
 * @author rain
 * @date 7/21/2024 3:37 PM
 */
@Data
public class MjPlayer {
    private User user;
    private List<MjLog> mjLogs; // recent game logs

    public MjPlayer(User user, List<MjLog> mjLogs) {
        this.user = user;
        this.mjLogs = mjLogs;
    }

    public LocalDateTime getLastGameTime() {
        if (mjLogs.isEmpty()) {
            return null;
        }

        Optional<MjLog> latestMjLogOptional = mjLogs.stream().max(Comparator.nullsLast(Comparator.comparing(MjLog::getCreateTime)));
        return latestMjLogOptional.map(MjLog::getCreateTime).orElse(null);
    }

    List<String> getTags() {
        return mjLogs.stream().map(MjLog::getTags).flatMap(List::stream).toList();
    }
}
