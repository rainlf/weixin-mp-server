package com.rainlf.weixin.domain.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.rainlf.weixin.app.dto.MahjongLogDto;
import com.rainlf.weixin.app.dto.MahjongRoundInfoDto;
import com.rainlf.weixin.app.dto.SportInfoDto;
import com.rainlf.weixin.app.dto.UserMahjongTagDto;
import com.rainlf.weixin.domain.consts.GameDetailTypeEnum;
import com.rainlf.weixin.domain.consts.GameTypeEnum;
import com.rainlf.weixin.domain.consts.MahjongScoreExtEnum;
import com.rainlf.weixin.domain.consts.MahjongWinCaseEnum;
import com.rainlf.weixin.domain.service.GameService;
import com.rainlf.weixin.infra.db.model.*;
import com.rainlf.weixin.infra.db.repository.*;
import com.rainlf.weixin.infra.util.JsonUtils;
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
    private GameRepository gameRepository;
    @Autowired
    private GameDetailRepository gameDetailRepository;
    @Autowired
    private MahjongPlayerRepository mahjongPlayerRepository;

    @Value("${recorder.award.random.max}")
    private int randomAwardMax;

    private int userTagListMaxLen = 5;

    private int gameDetailPageSize = 100;


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
    public void saveMahjongInfo(MahjongRoundInfoDto mahjongRoundInfoDto) {
        Game game = new Game();
        game.setGameType(GameTypeEnum.MAHJONG.ordinal());
        game.setRecorderId(mahjongRoundInfoDto.getRecorderId());
        game.setScore(mahjongRoundInfoDto.getBaseFan());
        game.setWinCase(mahjongRoundInfoDto.getWinCase().toString());
        game.setScoreExt(JsonUtils.toJson(mahjongRoundInfoDto.getFanList()));
        game = gameRepository.save(game);

        int fan = calculateMahjongTotalFan(mahjongRoundInfoDto.getBaseFan(), mahjongRoundInfoDto.getFanList().size());
        log.info("total fan: {}", fan);

        List<User> users = new ArrayList<>();
        Map<Integer, Integer> socreMap = new HashMap<>();
        List<User> winners = userRepository.findAllById(mahjongRoundInfoDto.getWinnerIds());
        List<User> losers = userRepository.findAllById(mahjongRoundInfoDto.getLoserIds());

        int winnerNumber = mahjongRoundInfoDto.getWinCase().getWinnerNumber();
        int loserNumber = mahjongRoundInfoDto.getWinCase().getLoserNumber();
        if (winners.size() != winnerNumber || losers.size() != loserNumber) {
            throw new RuntimeException("not expect scenario");
        }

        users.addAll(winners);
        users.addAll(losers);
        winners.forEach(winner -> socreMap.put(winner.getId(), fan * loserNumber));
        losers.forEach(loser -> socreMap.put(loser.getId(), -fan * winnerNumber));
        log.info("socre info: {}", socreMap);

        savePlayerDetail(game.getId(), users, socreMap);
        log.info("save game detail success");

        int award = new Random().nextInt(randomAwardMax) + 1;
        log.info("recorder info, userId: {}, score: {}", mahjongRoundInfoDto.getRecorderId(), award);
        saveRecorderDetail(game.getId(), mahjongRoundInfoDto.getRecorderId(), award);
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
        List<Game> games = gameRepository.findAll();
        List<Integer> gameIds = games.stream().map(Game::getId).toList();

        // find game detail
        List<GameDetail> gameDetails = gameDetailRepository.findByGameIdIn(gameIds);
        List<Integer> userId = gameDetails.stream().map(GameDetail::getUserId).toList();
        Map<Integer/* gameId */, List<GameDetail>> gameDetailsMap = gameDetails.stream().collect(Collectors.groupingBy(GameDetail::getGameId));

        // find user
        List<User> users = userRepository.findAllById(userId);
        Map<Integer/* userId */, User> userMap = users.stream().collect(Collectors.toMap(User::getId, x -> x));

        // create mahjong log list
        List<MahjongLogDto> result = new ArrayList<>();
        for (Game game : games) {
            Integer gameId = game.getId();
            MahjongWinCaseEnum mahjongWinCaseEnum = MahjongWinCaseEnum.valueOf(game.getWinCase());
            List<MahjongScoreExtEnum> mahjongScoreExtEnums = JsonUtils.toObjectList(game.getScoreExt(), new TypeReference<>() {
            });


            // create mahjong log
            MahjongLogDto mahjongLogDto = new MahjongLogDto();
            mahjongLogDto.setGameId(gameId);

            // set tags
            List<String> tags = new ArrayList<>();
            if (mahjongWinCaseEnum != MahjongWinCaseEnum.MJ_COMMON_WIN) {
                tags.add(mahjongWinCaseEnum.getName());
            }
            tags.addAll(mahjongScoreExtEnums.stream().map(MahjongScoreExtEnum::getName).toList());
            mahjongLogDto.setGameTags(tags);


            // find all details in game
            List<GameDetail> gameDetailList = gameDetailsMap.get(gameId);


            // find recorder
            GameDetail recorderDetail = gameDetailList.stream().filter(x -> x.getType() == GameDetailTypeEnum.MAHJONG_AWARD.ordinal()).findFirst().orElseThrow();
            User recorder = userMap.get(recorderDetail.getUserId());
            mahjongLogDto.setRecorderId(recorder.getId());
            mahjongLogDto.setRecorderName(recorder.getNickname());
            mahjongLogDto.setRecorderAvatar(recorder.getAvatar());
            mahjongLogDto.setRecorderAward(recorderDetail.getScore());

            // find winner
            List<GameDetail> winnerDetailList = gameDetailList.stream().filter(x -> x.getType() == GameDetailTypeEnum.MAHJONG_GAME.ordinal()).filter(x -> x.getScore() > 0).toList();
            List<MahjongLogDto.Item> winners = new ArrayList<>();
            for (GameDetail winnerDetail : winnerDetailList) {
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
            List<GameDetail> loserDetailList = gameDetailList.stream().filter(x -> x.getType() == GameDetailTypeEnum.MAHJONG_GAME.ordinal()).filter(x -> x.getScore() < 0).toList();
            List<MahjongLogDto.Item> losers = new ArrayList<>();
            for (GameDetail loserDetail : loserDetailList) {
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
            List<GameDetail> gameDetails = gameDetailRepository.findByUserIdAndType(userId, GameDetailTypeEnum.MAHJONG_GAME.ordinal());
            List<Integer> gameIds = gameDetails.stream().map(GameDetail::getGameId).toList();

            // find game
            if (!gameIds.isEmpty()) {
                List<Game> games = gameRepository.findByIdIn(gameIds);

                UserMahjongTagDto userMahjongTagDto = new UserMahjongTagDto();
                userMahjongTagDto.setUserId(userId);
                List<String> tags = new ArrayList<>();
                for (Game game : games) {
                    // find tags
                    MahjongWinCaseEnum mahjongWinCaseEnum = MahjongWinCaseEnum.valueOf(game.getWinCase());
                    List<MahjongScoreExtEnum> mahjongScoreExtEnums = JsonUtils.toObjectList(game.getScoreExt(), new TypeReference<>() {
                    });
                    if (mahjongWinCaseEnum != MahjongWinCaseEnum.MJ_COMMON_WIN) {
                        tags.add(mahjongWinCaseEnum.getName());
                    }
                    tags.addAll(mahjongScoreExtEnums.stream().map(MahjongScoreExtEnum::getName).toList());

                    userMahjongTagDto.setTags(tags.subList(0, Math.min(tags.size(), userTagListMaxLen)));
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
        saveMultiGameDetail(gameId, GameDetailTypeEnum.MAHJONG_GAME, users, socreMap);
    }

    /**
     * change recorder asset and save game detail
     */
    private void saveRecorderDetail(Integer gameId, Integer userId, Integer score) {
        saveSingleGameDetail(gameId, GameDetailTypeEnum.MAHJONG_AWARD, userId, score);
    }

    /**
     * change sporter asset and save game detail
     */
    private void saveSporterDetail(Integer userId, Integer score) {
        saveSingleGameDetail(null, GameDetailTypeEnum.REPAYMENT_SPORT, userId, score);
    }

    /**
     * change single user asset and save game detail
     */
    private void saveSingleGameDetail(Integer gameId, GameDetailTypeEnum type, Integer userId, Integer score) {
        User user = userRepository.findById(userId).orElseThrow();
        List<User> users = Collections.singletonList(user);
        Map<Integer, Integer> socreMap = new HashMap<>();
        socreMap.put(user.getId(), score);
        saveMultiGameDetail(gameId, type, users, socreMap);
    }

    /**
     * change multi user asset and save game detail
     */
    private void saveMultiGameDetail(Integer gameId, GameDetailTypeEnum type, List<User> users, Map<Integer/* userId */, Integer/* userScore */> socreMap) {
        // find asset
        List<UserAsset> userAssets = userAssetRepository.findByUserIdIn(users.stream().map(User::getId).toList());
        Map<Integer, UserAsset> userAssetMap = userAssets.stream().collect(Collectors.toMap(UserAsset::getUserId, x -> x));

        // change asset and insert game details
        List<GameDetail> gameDetails = new ArrayList<>();
        users.forEach(user -> {
            Integer userId = user.getId();
            // change user asset
            UserAsset userAsset = userAssetMap.get(userId);
            userAsset.setCopperCoin(userAsset.getCopperCoin() + socreMap.get(userId));

            // insert game details
            GameDetail detail = new GameDetail();
            detail.setGameId(gameId);
            detail.setUserId(userId);
            detail.setType(type.ordinal());
            detail.setScore(socreMap.get(userId));
            gameDetails.add(detail);
        });

        // save db
        userAssetRepository.saveAll(userAssets);
        gameDetailRepository.saveAll(gameDetails);
    }

    /**
     * calculate mahjong total fan
     */
    private int calculateMahjongTotalFan(int fan, int fanListSize) {
        return fan << fanListSize;
    }
}
