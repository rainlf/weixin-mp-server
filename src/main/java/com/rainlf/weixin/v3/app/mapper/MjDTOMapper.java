package com.rainlf.weixin.v3.app.mapper;

import com.rainlf.weixin.v3.app.dto.MjGameLogDTO;
import com.rainlf.weixin.v3.app.dto.MjPlayerDTO;
import com.rainlf.weixin.v3.app.dto.MjRankDTO;
import com.rainlf.weixin.v3.domain.mahjong.model.MjGameLog;
import com.rainlf.weixin.v3.domain.mahjong.model.MjPlayer;
import com.rainlf.weixin.v3.domain.mahjong.model.MjUserLog;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/**
 * @author rain
 * @date 7/21/2024 3:16 PM
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface MjDTOMapper {

    default List<MjGameLogDTO> fromMjGameLogs(List<MjGameLog> mjGameLogs) {
        List<MjGameLogDTO> mjGameLogDTOS = new ArrayList<>();

        for (MjGameLog mjGameLog : mjGameLogs) {
            MjGameLogDTO mjGameLogDTO = new MjGameLogDTO();

            String gameId = mjGameLog.getGameId();
            List<MjUserLog> mjUserLogs = mjGameLog.getMjLogs();
            List<MjUserLog> winerLogs = mjUserLogs.stream().filter(MjUserLog::isWinner).toList();
            List<MjUserLog> loserLogs = mjUserLogs.stream().filter(MjUserLog::isLoser).toList();
            Optional<MjUserLog> recorderLogOptional = mjUserLogs.stream().filter(MjUserLog::isRecorder).findFirst();

            if (recorderLogOptional.isEmpty() || winerLogs.isEmpty() || loserLogs.isEmpty()) {
                continue;
            }

            // recorder
            MjUserLog recorderLog = recorderLogOptional.get();
            LocalDateTime createTime = recorderLog.getMjLog().getCreateTime();
            boolean canDelete = LocalDateTime.now().isBefore(createTime.plusHours(1L)); // 1h 内可删除
            mjGameLogDTO.setGameId(gameId);
            mjGameLogDTO.setCanDelete(canDelete);
            mjGameLogDTO.setCreateTime(createTime);
            mjGameLogDTO.setRecordUser(new MjGameLogDTO.Player(recorderLog));

            // winners
            List<MjGameLogDTO.Player> winners = winerLogs.stream().map(MjGameLogDTO.Player::new).toList();
            mjGameLogDTO.setWinners(winners);

            // losers
            List<MjGameLogDTO.Player> loser = winerLogs.stream().map(MjGameLogDTO.Player::new).toList();
            mjGameLogDTO.setLosers(loser);

            mjGameLogDTOS.add(mjGameLogDTO);
        }
        return mjGameLogDTOS;
    }

    default List<MjPlayerDTO> fromMjPlayers(List<MjPlayer> mjPlayers) {
        return mjPlayers.stream().map(MjPlayerDTO::new).toList();
    }

    default MjRankDTO getMjRankDTOFromMjPlayers(List<MjPlayer> mjPlayers) {
        List<MjPlayer> zeroMjPlayers = mjPlayers.stream().filter(mjPlayer -> mjPlayer.getUser().getCoin() == 0).toList();
        List<MjPlayer> noneZeroMjPlayers = mjPlayers.stream().filter(mjPlayer -> mjPlayer.getUser().getCoin() != 0).toList();

        // sort by coin desc, last game time desc
        zeroMjPlayers.sort(Comparator.comparing((MjPlayer x) -> x.getUser().getCoin()).reversed());
        noneZeroMjPlayers.sort(Comparator.comparing(MjPlayer::getLastGameTime).reversed());

        List<MjPlayer> mjPlayersSorted = new ArrayList<>();
        mjPlayersSorted.addAll(zeroMjPlayers);
        mjPlayersSorted.addAll(noneZeroMjPlayers);

        MjRankDTO mjRankDTO = new MjRankDTO();
        if (!mjPlayersSorted.isEmpty()) {
            MjPlayer topMjPlayer = mjPlayersSorted.get(0);
            MjPlayer bottomMjPlayer = mjPlayersSorted.get(mjPlayersSorted.size() - 1);

            mjRankDTO.setTopUserId(topMjPlayer.getUser().getId());
            mjRankDTO.setTopUserNickname(topMjPlayer.getUser().getNickname());
            mjRankDTO.setTopUserCoin(topMjPlayer.getUser().getCoin());

            mjRankDTO.setBottomUserId(bottomMjPlayer.getUser().getId());
            mjRankDTO.setBottomUserNickname(bottomMjPlayer.getUser().getNickname());
            mjRankDTO.setBottomUserCoin(bottomMjPlayer.getUser().getCoin());

            List<MjRankDTO.RankItem> rankItems = mjPlayersSorted.stream().map(mjPlayer -> {
                Integer userId = mjPlayer.getUser().getId();
                String userNickname = mjPlayer.getUser().getNickname();
                Integer userCoin = mjPlayer.getUser().getCoin();
                List<String> userTags = mjPlayer.getMjLogs().stream().flatMap(mjLog -> mjLog.getTags().stream()).distinct().limit(10).toList();
                LocalDateTime lastGameTime = mjPlayer.getLastGameTime();
                return new MjRankDTO.RankItem(userId, userNickname, userCoin, userTags, lastGameTime);
            }).toList();
            mjRankDTO.setRankItems(rankItems);
        }
        return mjRankDTO;
    }
}
