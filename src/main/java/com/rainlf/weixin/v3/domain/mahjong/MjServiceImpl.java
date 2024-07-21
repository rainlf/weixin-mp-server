package com.rainlf.weixin.v3.domain.mahjong;

import com.rainlf.weixin.v3.app.dto.MjLogDTO;
import com.rainlf.weixin.v3.app.dto.MjRankDTO;
import com.rainlf.weixin.v3.domain.mahjong.consts.MjPointOperatorEnum;
import com.rainlf.weixin.v3.domain.user.UserService;
import com.rainlf.weixin.v3.infa.db.entity.MjLog;
import com.rainlf.weixin.v3.infa.db.entity.User;
import com.rainlf.weixin.v3.infa.db.repository.MjLogRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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
    public List<MjLogDTO> getMjLogs() {
        List<MjLog> mjLogs = mjLogRepository.findAll();

        // create dto by log
        Map<String, List<MjLog>> gameMjLogMap = mjLogs.stream().collect(Collectors.groupingBy(MjLog::getGameId));
        List<MjLogDTO> mjLogDTOs = new ArrayList<>();
        for (Map.Entry<String, List<MjLog>> entry : gameMjLogMap.entrySet()) {
            MjLogDTO mjLogDTO = new MjLogDTO();
            String gameId = entry.getKey();
            List<MjLog> logs = entry.getValue();
            List<MjLog> winerLogs = logs.stream().filter(MjLog::isWinner).toList();
            List<MjLog> loserLogs = logs.stream().filter(MjLog::isLoser).toList();
            Optional<MjLog> recorderLogOptional = logs.stream().filter(MjLog::isRecorder).findFirst();

            if (recorderLogOptional.isEmpty() || winerLogs.isEmpty() || loserLogs.isEmpty()) {
                log.warn("gameId:{} has no recorder log or winer log or loser log", gameId);
                continue;
            }

            // recorder
            MjLog recorderLog = recorderLogOptional.get();
            LocalDateTime createTime = recorderLog.getCreateTime();
            boolean canDelete = LocalDateTime.now().isBefore(createTime.plusHours(1L)); // 1h 内可删除
            mjLogDTO.setGameId(gameId);
            mjLogDTO.setCanDelete(canDelete);
            mjLogDTO.setCreateTime(createTime);
            User recordUser = userService.findById(recorderLog.getUserId());
            mjLogDTO.setRecordUser(new MjLogDTO.Player(recordUser.getId(), recordUser.getNickname(), recorderLog.getScore()));

            // winners
            List<MjLogDTO.Player> winners = new ArrayList<>();
            for (MjLog winerLog : winerLogs) {
                User winer = userService.findById(winerLog.getUserId());
                List<String> tags = winerLog.getPointOperators().stream().map(MjPointOperatorEnum::getName).toList();
                if (!winerLog.getGameType().isHide()) {
                    tags.add(winerLog.getGameType().getName());
                }
                winners.add(new MjLogDTO.Player(winer.getId(), winer.getNickname(), winerLog.getScore(), tags));
            }
            mjLogDTO.setWinners(winners);

            // losers
            List<MjLogDTO.Player> losers = new ArrayList<>();
            for (MjLog loserLog : loserLogs) {
                User loser = userService.findById(loserLog.getUserId());
                List<String> tags = new ArrayList<>();
                if (!loserLog.getGameType().isHide()) {
                    tags.add(loserLog.getGameType().getName());
                }
                losers.add(new MjLogDTO.Player(loser.getId(), loser.getNickname(), loserLog.getScore(), tags));
            }
            mjLogDTO.setLosers(losers);

        }


        return List.of();
    }

    @Override
    public MjRankDTO getMjRanks() {
        return null;
    }
}
