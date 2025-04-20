package com.pl.premier_zone.player.statistics;

import com.pl.premier_zone.player.Player;
import com.pl.premier_zone.player.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class PlayerStatisticsService {

    private final PlayerStatisticsRepository playerStatisticsRepository;
    private final PlayerRepository playerRepository;

    @Autowired
    public PlayerStatisticsService(PlayerStatisticsRepository playerStatisticsRepository, 
                                  PlayerRepository playerRepository) {
        this.playerStatisticsRepository = playerStatisticsRepository;
        this.playerRepository = playerRepository;
    }

    public List<PlayerStatistics> getAllPlayerStatistics() {
        return playerStatisticsRepository.findAll();
    }

    public Optional<PlayerStatistics> getPlayerStatisticsById(Long id) {
        return playerStatisticsRepository.findById(id);
    }

    public Optional<PlayerStatistics> getPlayerStatisticsByPlayerId(String playerId) {
        return playerStatisticsRepository.findByPlayerId(playerId);
    }

    public Optional<PlayerStatistics> getPlayerStatisticsByPlayerName(String playerName) {
        return playerStatisticsRepository.findByPlayerName(playerName);
    }

    @Transactional
    public PlayerStatistics createPlayerStatistics(String playerId, PlayerStatistics statistics) {
        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new IllegalArgumentException("Player not found with ID: " + playerId));
        
        statistics.setPlayer(player);
        return playerStatisticsRepository.save(statistics);
    }

    @Transactional
    public PlayerStatistics updatePlayerStatistics(Long id, PlayerStatistics updatedStatistics) {
        PlayerStatistics existingStatistics = playerStatisticsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Player statistics not found with ID: " + id));
        
        existingStatistics.setGamesPlayed(updatedStatistics.getGamesPlayed());
        existingStatistics.setGoals(updatedStatistics.getGoals());
        existingStatistics.setAssists(updatedStatistics.getAssists());
        existingStatistics.setYellowCards(updatedStatistics.getYellowCards());
        existingStatistics.setRedCards(updatedStatistics.getRedCards());
        existingStatistics.setMinutesPlayed(updatedStatistics.getMinutesPlayed());
        
        return playerStatisticsRepository.save(existingStatistics);
    }

    @Transactional
    public void deletePlayerStatistics(Long id) {
        playerStatisticsRepository.deleteById(id);
    }
} 