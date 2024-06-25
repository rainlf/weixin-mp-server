package com.rainlf.weixin.domain.service.impl;

import com.rainlf.weixin.app.dto.MahjongLogDto;
import com.rainlf.weixin.app.dto.MahjongGameDto;
import com.rainlf.weixin.app.dto.SportInfoDto;
import com.rainlf.weixin.app.dto.UserMahjongTagDto;
import com.rainlf.weixin.domain.consts.UserGameScoreTypeEnum;
import com.rainlf.weixin.domain.consts.MahjongFanEnum;
import com.rainlf.weixin.domain.consts.MahjongWinCaseEnum;
import com.rainlf.weixin.domain.service.GameService;
import com.rainlf.weixin.infra.db.model.*;
import com.rainlf.weixin.infra.db.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author rain
 * @date 6/14/2024 8:32 PM
 */
@Slf4j
@Service
public class GameServiceImpl implements GameService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserAssetRepository userAssetRepository;
    @Autowired
    private MahjongGameRepository mahjongGameRepository;
    @Autowired
    private UserScoreRepository userScoreRepository;
    @Autowired
    private MahjongPlayerRepository mahjongPlayerRepository;

    @Value("${recorder.award.random.max}")
    private int randomAwardMax;

    private final int userTagListMaxLen = 5;

    private final int gameDetailPageSize = 100;


    @Override
    public void addMahjongPlayer(Integer id) {
        List<MahjongPlayer> mahjongPlayers = mahjongPlayerRepository.findByUserId(id);
        mahjongPlayers.forEach(x -> x.setDeleted(true));

        MahjongPlayer mahjongPlayer = new MahjongPlayer();
        mahjongPlayer.setUserId(id);
        mahjongPlayers.add(mahjongPlayer);

        mahjongPlayerRepository.saveAll(mahjongPlayers);
    }

    @Override
    public void deleteMahjongPlayer(Integer id) {
        List<MahjongPlayer> mahjongPlayers = mahjongPlayerRepository.findByUserId(id);
        mahjongPlayers.forEach(x -> x.setDeleted(true));
        mahjongPlayerRepository.saveAll(mahjongPlayers);
    }

    @Override
    public List<Integer> getMahjongPlayerIds() {
        List<MahjongPlayer> mahjongPlayers = mahjongPlayerRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
        return mahjongPlayers.stream().map(MahjongPlayer::getUserId).collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveMahjongInfo(MahjongGameDto mahjongGameDto) {
        MahjongGame mahjongGame = new MahjongGame();
        mahjongGame.setRecorderId(mahjongGameDto.getRecorderId());
        mahjongGame.setScore(mahjongGameDto.getBaseFan());
        mahjongGame.setWinCase(mahjongGameDto.getWinCase());
        mahjongGame.setScoreExt(mahjongGameDto.getFanList());
        mahjongGame = mahjongGameRepository.save(mahjongGame);

        int fan = calculateMahjongTotalFan(mahjongGameDto.getBaseFan(), mahjongGameDto.getFanList().size());
        log.info("total fan: {}", fan);

        List<User> users = new ArrayList<>();
        Map<Integer, Integer> socreMap = new HashMap<>();
        List<User> winners = userRepository.findAllById(mahjongGameDto.getWinnerIds());
        List<User> losers = userRepository.findAllById(mahjongGameDto.getLoserIds());

        int winnerNumber = mahjongGameDto.getWinCase().getWinnerNumber();
        int loserNumber = mahjongGameDto.getWinCase().getLoserNumber();
        if (winners.size() != winnerNumber || losers.size() != loserNumber) {
            throw new RuntimeException("not expect scenario");
        }

        users.addAll(winners);
        users.addAll(losers);
        winners.forEach(winner -> socreMap.put(winner.getId(), fan * loserNumber));
        losers.forEach(loser -> socreMap.put(loser.getId(), -fan * winnerNumber));
        log.info("socre info: {}", socreMap);

        savePlayerDetail(mahjongGame.getId(), users, socreMap);
        log.info("save game detail success");

        int award = new Random().nextInt(randomAwardMax) + 1;
        log.info("recorder info, userId: {}, score: {}", mahjongGameDto.getRecorderId(), award);
        saveRecorderDetail(mahjongGame.getId(), mahjongGameDto.getRecorderId(), award);
        log.info("save recorder detail success");
    }


    @Override
    public void saveSportInfo(SportInfoDto sportInfoDto) {
        log.info("sport info, userId: {}, score: {}", sportInfoDto.getSporterId(), sportInfoDto.getSportNumber());
        saveSporterDetail(sportInfoDto.getSporterId(), sportInfoDto.getSportNumber());
        log.info("save sporter detail success");
    }

    @Override
    public List<MahjongLogDto> getMahjongLogs() {
        // find game
        List<MahjongGame> mahjongGames = mahjongGameRepository.findAll();
        List<Integer> gameIds = mahjongGames.stream().map(MahjongGame::getId).toList();

        // find game detail
        List<UserGameScore> userGameScoreHistories = userScoreRepository.findByGameIdIn(gameIds);
        List<Integer> userId = userGameScoreHistories.stream().map(UserGameScore::getUserId).toList();
        Map<Integer/* gameId */, List<UserGameScore>> gameDetailsMap = userGameScoreHistories.stream().collect(Collectors.groupingBy(UserGameScore::getGameId));

        // find user
        List<User> users = userRepository.findAllById(userId);
        Map<Integer/* userId */, User> userMap = users.stream().collect(Collectors.toMap(User::getId, x -> x));

        // create mahjong log list
        List<MahjongLogDto> result = new ArrayList<>();
        for (MahjongGame mahjongGame : mahjongGames) {
            Integer gameId = mahjongGame.getId();
            MahjongWinCaseEnum mahjongWinCaseEnum = mahjongGame.getWinCase();
            List<MahjongFanEnum> mahjongFanEnums = mahjongGame.getScoreExt();


            // create mahjong log
            MahjongLogDto mahjongLogDto = new MahjongLogDto();
            mahjongLogDto.setGameId(gameId);

            // set tags
            List<String> tags = new ArrayList<>();
            if (mahjongWinCaseEnum != MahjongWinCaseEnum.MJ_COMMON_WIN) {
                tags.add(mahjongWinCaseEnum.getName());
            }
            tags.addAll(mahjongFanEnums.stream().map(MahjongFanEnum::getName).toList());
            mahjongLogDto.setGameTags(tags);


            // find all details in game
            List<UserGameScore> userGameScoreList = gameDetailsMap.get(gameId);


            // find recorder
            UserGameScore recorderDetail = userGameScoreList.stream().filter(x -> x.getType() == UserGameScoreTypeEnum.MAHJONG_AWARD).findFirst().orElseThrow();
            User recorder = userMap.get(recorderDetail.getUserId());
            mahjongLogDto.setRecorderId(recorder.getId());
            mahjongLogDto.setRecorderName(recorder.getNickname());
            mahjongLogDto.setRecorderAvatar(recorder.getAvatar());
            mahjongLogDto.setRecorderAward(recorderDetail.getScore());

            // find winner
            List<UserGameScore> winnerDetailList = userGameScoreList.stream().filter(x -> x.getType() == UserGameScoreTypeEnum.MAHJONG_GAME).filter(x -> x.getScore() > 0).toList();
            List<MahjongLogDto.Item> winners = new ArrayList<>();
            for (UserGameScore winnerDetail : winnerDetailList) {
                User winner = userMap.get(winnerDetail.getUserId());
                MahjongLogDto.Item winnerItem = new MahjongLogDto.Item();
                winnerItem.setUserId(winner.getId());
                winnerItem.setUserName(winner.getNickname());
                winnerItem.setUserAvatar(winner.getAvatar());
                winnerItem.setScore(winnerDetail.getScore());
                winners.add(winnerItem);
            }
            mahjongLogDto.setWinners(winners);

            // find loser
            List<UserGameScore> loserDetailList = userGameScoreList.stream().filter(x -> x.getType() == UserGameScoreTypeEnum.MAHJONG_GAME).filter(x -> x.getScore() < 0).toList();
            List<MahjongLogDto.Item> losers = new ArrayList<>();
            for (UserGameScore loserDetail : loserDetailList) {
                User loser = userMap.get(loserDetail.getUserId());
                MahjongLogDto.Item loserItem = new MahjongLogDto.Item();
                loserItem.setUserId(loser.getId());
                loserItem.setUserName(loser.getNickname());
                loserItem.setUserAvatar(loser.getAvatar());
                loserItem.setScore(loserDetail.getScore());
                losers.add(loserItem);
            }
            mahjongLogDto.setLosers(losers);
            result.add(mahjongLogDto);
        }

        result.sort((a, b) -> b.getGameId() - a.getGameId());
        return result;
    }

    @Override
    public List<UserMahjongTagDto> getUserMahjongTags(List<Integer> userIds) {
        List<UserMahjongTagDto> result = new ArrayList<>();
        for (Integer userId : userIds) {
            // find game detail, get game id
            Pageable pageable = PageRequest.of(0, gameDetailPageSize, Sort.by(Sort.Direction.DESC, "id"));
//            List<GameDetail> gameDetails = gameDetailRepository.findByUserIdAndType(userId, 0, pageable);
            List<UserGameScore> userGameScoreHistories = userScoreRepository.findByUserIdAndType(userId, UserGameScoreTypeEnum.MAHJONG_GAME);
            List<Integer> gameIds = userGameScoreHistories.stream().map(UserGameScore::getGameId).toList();

            // find game
            if (!gameIds.isEmpty()) {
                List<MahjongGame> mahjongGames = mahjongGameRepository.findByIdIn(gameIds);

                UserMahjongTagDto userMahjongTagDto = new UserMahjongTagDto();
                userMahjongTagDto.setUserId(userId);
                List<String> tags = new ArrayList<>();
                for (MahjongGame mahjongGame : mahjongGames) {
                    // find tags
                    MahjongWinCaseEnum mahjongWinCaseEnum = mahjongGame.getWinCase();
                    List<MahjongFanEnum> mahjongFanEnums = mahjongGame.getScoreExt();
                    if (mahjongWinCaseEnum != MahjongWinCaseEnum.MJ_COMMON_WIN) {
                        tags.add(mahjongWinCaseEnum.getName());
                    }
                    tags.addAll(mahjongFanEnums.stream().map(MahjongFanEnum::getName).toList());

                    if (tags.size() >= userTagListMaxLen) {
                        userMahjongTagDto.setTags(tags.subList(0, userTagListMaxLen));
                        break;
                    }
                }
                result.add(userMahjongTagDto);
            }
        }

        return result;
    }

    /**
     * change recorder asset and save game detail
     */
    private void savePlayerDetail(Integer gameId, List<User> users, Map<Integer/* userId */, Integer/* userScore */> socreMap) {
        saveMultiGameDetail(gameId, UserGameScoreTypeEnum.MAHJONG_GAME, users, socreMap);
    }

    /**
     * change recorder asset and save game detail
     */
    private void saveRecorderDetail(Integer gameId, Integer userId, Integer score) {
        saveSingleGameDetail(gameId, UserGameScoreTypeEnum.MAHJONG_AWARD, userId, score);
    }

    /**
     * change sporter asset and save game detail
     */
    private void saveSporterDetail(Integer userId, Integer score) {
        saveSingleGameDetail(null, UserGameScoreTypeEnum.REPAYMENT_SPORT, userId, score);
    }

    /**
     * change single user asset and save game detail
     */
    private void saveSingleGameDetail(Integer gameId, UserGameScoreTypeEnum type, Integer userId, Integer score) {
        User user = userRepository.findById(userId).orElseThrow();
        List<User> users = Collections.singletonList(user);
        Map<Integer, Integer> socreMap = new HashMap<>();
        socreMap.put(user.getId(), score);
        saveMultiGameDetail(gameId, type, users, socreMap);
    }

    /**
     * change multi user asset and save game detail
     */
    private void saveMultiGameDetail(Integer gameId, UserGameScoreTypeEnum type, List<User> users, Map<Integer/* userId */, Integer/* userScore */> socreMap) {
        // find asset
        List<UserAsset> userAssets = userAssetRepository.findByUserIdIn(users.stream().map(User::getId).toList());
        Map<Integer, UserAsset> userAssetMap = userAssets.stream().collect(Collectors.toMap(UserAsset::getUserId, x -> x));

        // change asset and insert game details
        List<UserGameScore> userGameScoreHistories = new ArrayList<>();
        users.forEach(user -> {
            Integer userId = user.getId();
            // change user asset
            UserAsset userAsset = userAssetMap.get(userId);
            userAsset.setCopperCoin(userAsset.getCopperCoin() + socreMap.get(userId));

            // insert game details
            UserGameScore detail = new UserGameScore();
            detail.setGameId(gameId);
            detail.setUserId(userId);
            detail.setType(type);
            detail.setScore(socreMap.get(userId));
            userGameScoreHistories.add(detail);
        });

        // save db
        userAssetRepository.saveAll(userAssets);
        userScoreRepository.saveAll(userGameScoreHistories);
    }

    /**
     * calculate mahjong total fan
     */
    private int calculateMahjongTotalFan(int fan, int fanListSize) {
        return fan << fanListSize;
    }
}
