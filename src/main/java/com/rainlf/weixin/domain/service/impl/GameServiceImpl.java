package com.rainlf.weixin.domain.service.impl;

import com.rainlf.weixin.app.dto.MahjongGameDto;
import com.rainlf.weixin.app.dto.MahjongLogDto;
import com.rainlf.weixin.app.dto.SportInfoDto;
import com.rainlf.weixin.app.dto.UserMahjongTagDto;
import com.rainlf.weixin.domain.consts.MahjongFanEnum;
import com.rainlf.weixin.domain.consts.MahjongWinCaseEnum;
import com.rainlf.weixin.domain.consts.UserScoreTypeEnum;
import com.rainlf.weixin.domain.service.GameService;
import com.rainlf.weixin.infra.db.entity.*;
import com.rainlf.weixin.infra.db.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
        mahjongGame.setRefereeUserId(mahjongGameDto.getRecorderId());
        mahjongGame.setBaseScore(mahjongGameDto.getBaseFan());
        mahjongGame.setWinCase(mahjongGameDto.getWinCase());
        mahjongGame.setFanList(mahjongGameDto.getFanList());
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
        List<UserScore> userScoreHistories = userScoreRepository.findByGameIdIn(gameIds);
        List<Integer> userId = userScoreHistories.stream().map(UserScore::getUserId).toList();
        Map<Integer/* gameId */, List<UserScore>> gameDetailsMap = userScoreHistories.stream().collect(Collectors.groupingBy(UserScore::getGameId));

        // find user
        List<User> users = userRepository.findAllById(userId);
        Map<Integer/* userId */, User> userMap = users.stream().collect(Collectors.toMap(User::getId, x -> x));

        // create mahjong log list
        List<MahjongLogDto> result = new ArrayList<>();
        for (MahjongGame mahjongGame : mahjongGames) {
            Integer gameId = mahjongGame.getId();
            MahjongWinCaseEnum mahjongWinCaseEnum = mahjongGame.getWinCase();
            List<MahjongFanEnum> mahjongFanEnums = mahjongGame.getFanList();

            // create mahjong log
            MahjongLogDto mahjongLogDto = new MahjongLogDto();
            mahjongLogDto.setGameId(gameId);
            mahjongLogDto.setCreateTime(mahjongGame.getCreateTime());

            // set tags
            List<String> tags = new ArrayList<>();
            if (mahjongWinCaseEnum != MahjongWinCaseEnum.MJ_COMMON_WIN) {
                tags.add(mahjongWinCaseEnum.getName());
            }
            tags.addAll(mahjongFanEnums.stream().map(MahjongFanEnum::getName).toList());
            mahjongLogDto.setGameTags(tags);


            // find all details in game
            List<UserScore> userScoreList = gameDetailsMap.get(gameId);


            // find recorder
            UserScore recorderDetail = userScoreList.stream().filter(x -> x.getType() == UserScoreTypeEnum.MAHJONG_AWARD).findFirst().orElse(null );
            if (recorderDetail != null) {
                User recorder = userMap.get(recorderDetail.getUserId());
                mahjongLogDto.setRecorderId(recorder.getId());
                mahjongLogDto.setRecorderName(recorder.getNickname());
                mahjongLogDto.setRecorderAvatar(recorder.getAvatar());
                mahjongLogDto.setRecorderAward(recorderDetail.getScore());
            }


            // find winner
            List<UserScore> winnerDetailList = userScoreList.stream().filter(x -> x.getType() == UserScoreTypeEnum.MAHJONG_GAME).filter(x -> x.getScore() > 0).toList();
            List<MahjongLogDto.Item> winners = new ArrayList<>();
            for (UserScore winnerDetail : winnerDetailList) {
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
            List<UserScore> loserDetailList = userScoreList.stream().filter(x -> x.getType() == UserScoreTypeEnum.MAHJONG_GAME).filter(x -> x.getScore() < 0).toList();
            List<MahjongLogDto.Item> losers = new ArrayList<>();
            for (UserScore loserDetail : loserDetailList) {
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
            List<UserScore> userScoreList = userScoreRepository.findByUserIdAndType(userId, UserScoreTypeEnum.MAHJONG_GAME);
            // last 10 games of user with winner case
            List<Integer> gameIds = userScoreList.stream()
                    .filter(x -> x.getScore() > 0)
                    .sorted(Comparator.comparing(UserScore::getCreateTime).reversed())
                    .limit(10)
                    .map(UserScore::getGameId)
                    .toList();

            List<MahjongGame> gameList = mahjongGameRepository.findByIdIn(gameIds);
            List<String> userTags = gameList.stream()
                    .flatMap(x -> {
                        List<String> gameTags = new ArrayList<>();
                        if (x.getWinCase() == MahjongWinCaseEnum.MJ_SELF_TOUCH_WIN) {
                            gameTags.add(x.getWinCase().getName());
                        }
                        gameTags.addAll(x.getFanList().stream().map(MahjongFanEnum::getName).toList());
                        return gameTags.stream();
                    })
                    .distinct()
                    .toList();

            UserMahjongTagDto dto = new UserMahjongTagDto();
            dto.setUserId(userId);
            dto.setTags(userTags);
            result.add(dto);
        }

        return result;
    }

    /**
     * change recorder asset and save game detail
     */
    private void savePlayerDetail(Integer gameId, List<User> users, Map<Integer/* userId */, Integer/* userScore */> socreMap) {
        saveMultiGameDetail(gameId, UserScoreTypeEnum.MAHJONG_GAME, users, socreMap);
    }

    /**
     * change recorder asset and save game detail
     */
    private void saveRecorderDetail(Integer gameId, Integer userId, Integer score) {
        saveSingleGameDetail(gameId, UserScoreTypeEnum.MAHJONG_AWARD, userId, score);
    }

    /**
     * change sporter asset and save game detail
     */
    private void saveSporterDetail(Integer userId, Integer score) {
        saveSingleGameDetail(null, UserScoreTypeEnum.REPAYMENT_SPORT, userId, score);
    }

    /**
     * change single user asset and save game detail
     */
    private void saveSingleGameDetail(Integer gameId, UserScoreTypeEnum type, Integer userId, Integer score) {
        User user = userRepository.findById(userId).orElseThrow();
        List<User> users = Collections.singletonList(user);
        Map<Integer, Integer> socreMap = new HashMap<>();
        socreMap.put(user.getId(), score);
        saveMultiGameDetail(gameId, type, users, socreMap);
    }

    /**
     * change multi user asset and save game detail
     */
    private void saveMultiGameDetail(Integer gameId, UserScoreTypeEnum type, List<User> users, Map<Integer/* userId */, Integer/* userScore */> socreMap) {
        // find asset
        List<UserAsset> userAssets = userAssetRepository.findByUserIdIn(users.stream().map(User::getId).toList());
        Map<Integer, UserAsset> userAssetMap = userAssets.stream().collect(Collectors.toMap(UserAsset::getUserId, x -> x));

        // change asset and insert game details
        List<UserScore> userScoreHistories = new ArrayList<>();
        users.forEach(user -> {
            Integer userId = user.getId();
            // change user asset
            UserAsset userAsset = userAssetMap.get(userId);
            userAsset.setCopperCoin(userAsset.getCopperCoin() + socreMap.get(userId));

            // insert game details
            UserScore detail = new UserScore();
            detail.setGameId(gameId);
            detail.setUserId(userId);
            detail.setType(type);
            detail.setScore(socreMap.get(userId));
            userScoreHistories.add(detail);
        });

        // save db
        userAssetRepository.saveAll(userAssets);
        userScoreRepository.saveAll(userScoreHistories);
    }

    /**
     * calculate mahjong total fan
     */
    private int calculateMahjongTotalFan(int fan, int fanListSize) {
        return fan << fanListSize;
    }
}
