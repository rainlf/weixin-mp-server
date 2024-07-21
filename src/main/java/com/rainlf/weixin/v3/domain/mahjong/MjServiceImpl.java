package com.rainlf.weixin.v3.domain.mahjong;

import com.rainlf.weixin.v3.domain.mahjong.model.MjGameLog;
import com.rainlf.weixin.v3.domain.mahjong.model.MjPlayer;
import com.rainlf.weixin.v3.domain.mahjong.model.MjUserLog;
import com.rainlf.weixin.v3.domain.user.UserService;
import com.rainlf.weixin.v3.infa.db.entity.MjLog;
import com.rainlf.weixin.v3.infa.db.entity.User;
import com.rainlf.weixin.v3.infa.db.repository.MjLogRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author rain
 * @date 7/20/2024 6:57 AM
 */
@Slf4j
@Service
public class MjServiceImpl implements MjService {
    @Autowired
    private MjLogRepository mjLogRepository;
    @Autowired
    private UserService userService;

    @Override
    public List<MjGameLog> getMjLogs() {
        List<MjLog> mjLogs = mjLogRepository.findAll();
        return getMjGameLogs(mjLogs);
    }

    @Override
    public List<MjGameLog> getUserMjLogs(Integer userId) {
        List<MjLog> mjLogs = mjLogRepository.findByUserId(userId);
        return getMjGameLogs(mjLogs);
    }

    private List<MjGameLog> getMjGameLogs(List<MjLog> mjLogs) {
        Map<Integer, User> userMap = userService.findByIdIn(mjLogs.stream().map(MjLog::getUserId).toList()).stream().collect(Collectors.toMap(User::getId, user -> user));
        Map<String, List<MjLog>> gameMjLogMap = mjLogs.stream().collect(Collectors.groupingBy(MjLog::getGameId));
        return gameMjLogMap.entrySet().stream().map(entry -> {
            String gameId = entry.getKey();
            List<MjUserLog> mjUserLogs = entry.getValue().stream().map(mjLog -> new MjUserLog(mjLog, userMap.get(mjLog.getUserId()))).toList();
            return new MjGameLog(gameId, mjUserLogs);
        }).toList();
    }

    @Override
    public List<MjPlayer> getMjPlayers() {
        List<User> users = userService.findAll();

        List<MjPlayer> mjPlayers = new ArrayList<>();
        for (User user : users) {
            List<MjLog> mjLogs = mjLogRepository.findLatest10LogWithTags(user.getId());
            MjPlayer mjPlayer = new MjPlayer(user, mjLogs);
            mjPlayers.add(mjPlayer);
        }
        return mjPlayers;
    }

    @Override
    public List<MjPlayer> getLatestMjPlayers() {
        List<MjLog> mjGameLog = mjLogRepository.findLatestMjGameLog();
        Map<Integer, User> userMap = userService.findByIdIn(mjGameLog.stream().map(MjLog::getUserId).toList())
                .stream().collect(Collectors.toMap(User::getId, user -> user));
        return mjGameLog.stream().map(mjLog -> new MjPlayer(userMap.get(mjLog.getUserId()), Collections.singletonList(mjLog))).toList();
    }
}
