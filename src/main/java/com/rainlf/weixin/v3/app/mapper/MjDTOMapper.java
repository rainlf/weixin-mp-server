package com.rainlf.weixin.v3.app.mapper;

import com.rainlf.weixin.v3.app.dto.MjLogDTO;
import com.rainlf.weixin.v3.domain.mahjong.model.MjGameLog;
import com.rainlf.weixin.v3.domain.mahjong.model.MjUserLog;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author rain
 * @date 7/21/2024 3:16 PM
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface MjDTOMapper {

    default List<MjLogDTO> fromMjGameLogs(List<MjGameLog> mjGameLogs) {
        List<MjLogDTO> mjLogDTOs = new ArrayList<>();

        for (MjGameLog mjGameLog : mjGameLogs) {
            MjLogDTO mjLogDTO = new MjLogDTO();

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
            mjLogDTO.setGameId(gameId);
            mjLogDTO.setCanDelete(canDelete);
            mjLogDTO.setCreateTime(createTime);
            mjLogDTO.setRecordUser(new MjLogDTO.Player(recorderLog));

            // winners
            List<MjLogDTO.Player> winners = winerLogs.stream().map(MjLogDTO.Player::new).toList();
            mjLogDTO.setWinners(winners);

            // losers
            List<MjLogDTO.Player> loser = winerLogs.stream().map(MjLogDTO.Player::new).toList();
            mjLogDTO.setLosers(loser);

            mjLogDTOs.add(mjLogDTO);
        }
        return mjLogDTOs;
    }
}
