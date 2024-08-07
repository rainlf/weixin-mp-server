package com.rainlf.weixin.v3.domain.mahjong;

import com.rainlf.weixin.v3.app.dto.DeskGameDTO;
import com.rainlf.weixin.v3.app.dto.OnlineGameDTO;
import com.rainlf.weixin.v3.domain.mahjong.consts.MjGameTypeEnum;
import com.rainlf.weixin.v3.domain.mahjong.consts.MjUserTypeEnum;
import com.rainlf.weixin.v3.domain.mahjong.model.MjGameLog;
import com.rainlf.weixin.v3.domain.mahjong.model.MjPlayer;
import com.rainlf.weixin.v3.domain.mahjong.model.MjUserLog;
import com.rainlf.weixin.v3.domain.user.UserService;
import com.rainlf.weixin.v3.infa.db.entity.MjLog;
import com.rainlf.weixin.v3.infa.db.entity.User;
import com.rainlf.weixin.v3.infa.db.repository.MjLogRepository;
import com.rainlf.weixin.v3.infa.util.MgttUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveOnlieGame(OnlineGameDTO onlineGameDTO) {
        String gameId = MgttUtils.getMjUUID();

        // winner and loser
        for (OnlineGameDTO.Item item : onlineGameDTO.getItems()) {
            User user = userService.findById(item.getUserId());
            user.addScore(item.getScore());
            userService.save(user);

            MjUserTypeEnum userTypeEnum = item.getScore() > 0 ? MjUserTypeEnum.WINNER : MjUserTypeEnum.LOSER;
            MjLog mjLog = new MjLog();
            mjLog.setGameId(gameId);
            mjLog.setUserId(item.getUserId());
            mjLog.setUserType(userTypeEnum);
            mjLog.setGameType(MjGameTypeEnum.ONLINE_MJ);
            mjLog.setScore(item.getScore());
            mjLogRepository.save(mjLog);
        }

        // recorder
        User user = userService.findById(onlineGameDTO.getRecordrId());
        user.addScore(MgttUtils.getRandAward());
        userService.save(user);
        MjLog mjLog = new MjLog();
        mjLog.setGameId(gameId);
        mjLog.setUserId(onlineGameDTO.getRecordrId());
        mjLog.setUserType(MjUserTypeEnum.RECORDER);
        mjLog.setGameType(MjGameTypeEnum.ONLINE_MJ);
        mjLog.setScore(MgttUtils.getRandAward());
        mjLogRepository.save(mjLog);
    }

    @Override
    public void saveDeskGame(DeskGameDTO deskGameDTO) {
        String gameId = MgttUtils.getMjUUID();

        // winner
        for (DeskGameDTO.Item item : deskGameDTO.getWinners()) {
            User user = userService.findById(item.getUserId());
            user.addScore(item.getScore());
            userService.save(user);

            MjLog mjLog = new MjLog();
            mjLog.setGameId(gameId);
            mjLog.setUserId(item.getUserId());
            mjLog.setUserType(MjUserTypeEnum.WINNER);
            mjLog.setGameType(deskGameDTO.getGameType());
            mjLog.setPoint(item.getPoint());
            mjLog.setPointOperators(item.getPointOperators());
            mjLog.setScore(item.getScore());
            mjLogRepository.save(mjLog);
        }

        // loser
        for (DeskGameDTO.Item item : deskGameDTO.getLosers()) {
            User user = userService.findById(item.getUserId());
            user.addScore(item.getScore());
            userService.save(user);

            MjLog mjLog = new MjLog();
            mjLog.setGameId(gameId);
            mjLog.setUserId(item.getUserId());
            mjLog.setUserType(MjUserTypeEnum.LOSER);
            mjLog.setGameType(deskGameDTO.getGameType());
            mjLog.setScore(item.getScore());
            mjLogRepository.save(mjLog);
        }

        // recorder
        User user = userService.findById(deskGameDTO.getRecordrId());
        user.addScore(MgttUtils.getRandAward());
        userService.save(user);
        MjLog mjLog = new MjLog();
        mjLog.setGameId(gameId);
        mjLog.setUserId(deskGameDTO.getRecordrId());
        mjLog.setUserType(MjUserTypeEnum.RECORDER);
        mjLog.setGameType(deskGameDTO.getGameType());
        mjLog.setScore(MgttUtils.getRandAward());
        mjLogRepository.save(mjLog);
    }
}
