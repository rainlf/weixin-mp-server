package com.rainlf.weixin.v3.domain.mahjong;

import com.rainlf.weixin.v3.app.dto.MjLogDTO;
import com.rainlf.weixin.v3.app.dto.MjRankDTO;
import com.rainlf.weixin.v3.domain.user.UserService;
import com.rainlf.weixin.v3.infa.db.entity.MjLog;
import com.rainlf.weixin.v3.infa.db.entity.User;
import com.rainlf.weixin.v3.infa.db.repository.MjLogRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
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
        return creatMjLogDTOs(mjLogs);
    }

    @Override
    public List<MjLogDTO> getUserMjLogs(Integer userId) {
        List<MjLog> mjLogs = mjLogRepository.findByUserId(userId);
        return creatMjLogDTOs(mjLogs);
    }

    @Override
    public MjRankDTO getMjRanks() {
        List<User> users = userService.findAll();

        // crete rank item wiht user tags
        List<MjRankDTO.RankItem> rankItems = new ArrayList<>();
        for (User user : users) {
            LocalDateTime lastGameTime = null;
            List<String> tags = new ArrayList<>();
            List<MjLog> mjLogs = mjLogRepository.findLast10LogWithTags(user.getId());
            if (!mjLogs.isEmpty()) {
                lastGameTime = mjLogs.get(0).getCreateTime();
                tags = mjLogs.stream().map(MjLog::getTags).flatMap(List::stream).toList();
            }
            MjRankDTO.RankItem item = new MjRankDTO.RankItem(user.getId(), user.getNickname(), user.getCoin(), tags, lastGameTime);
            rankItems.add(item);
        }

        List<MjRankDTO.RankItem> zeroImtes = rankItems.stream().filter(item -> item.getUserCoin() == 0).toList();
        List<MjRankDTO.RankItem> nonZeroItems = rankItems.stream().filter(item -> item.getUserCoin() != 0).toList();
        zeroImtes.sort(Comparator.nullsLast(Comparator.comparing(MjRankDTO.RankItem::getLastGameTime).reversed()));
        nonZeroItems.sort(Comparator.nullsLast(Comparator.comparing(MjRankDTO.RankItem::getUserCoin).reversed()));

        List<MjRankDTO.RankItem> sortedItems = new ArrayList<>();
        sortedItems.addAll(nonZeroItems);
        sortedItems.addAll(zeroImtes);

        return getMjRankDTO(sortedItems);
    }

    private MjRankDTO getMjRankDTO(List<MjRankDTO.RankItem> sortedItems) {
        MjRankDTO mjRankDTO = new MjRankDTO();
        if (!sortedItems.isEmpty()) {
            MjRankDTO.RankItem topItem = sortedItems.get(0);
            MjRankDTO.RankItem bottomItem = sortedItems.get(sortedItems.size() - 1);
            mjRankDTO.setTopUserId(topItem.getUserId());
            mjRankDTO.setTopUserNickname(topItem.getUserNickname());
            mjRankDTO.setTopUserCoin(topItem.getUserCoin());
            mjRankDTO.setBottomUserId(bottomItem.getUserId());
            mjRankDTO.setBottomUserNickname(bottomItem.getUserNickname());
            mjRankDTO.setBottomUserCoin(bottomItem.getUserCoin());
        }
        mjRankDTO.setRankItems(sortedItems);
        return mjRankDTO;
    }

    private List<MjLogDTO> creatMjLogDTOs(List<MjLog> mjLogs) {
        List<MjLogDTO> mjLogDTOs = new ArrayList<>();

        // create dto by log
        Map<String, List<MjLog>> gameMjLogMap = mjLogs.stream().collect(Collectors.groupingBy(MjLog::getGameId));
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
                winners.add(new MjLogDTO.Player(winer.getId(), winer.getNickname(), winerLog.getScore(), winerLog.getTags()));
            }
            mjLogDTO.setWinners(winners);

            // losers
            List<MjLogDTO.Player> losers = new ArrayList<>();
            for (MjLog loserLog : loserLogs) {
                User loser = userService.findById(loserLog.getUserId());
                losers.add(new MjLogDTO.Player(loser.getId(), loser.getNickname(), loserLog.getScore(), loserLog.getTags()));
            }
            mjLogDTO.setLosers(losers);
            mjLogDTOs.add(mjLogDTO);
        }

        // order by createTime desc
        mjLogDTOs.sort(Comparator.nullsLast(Comparator.comparing(MjLogDTO::getCreateTime).reversed()));
    }
}
