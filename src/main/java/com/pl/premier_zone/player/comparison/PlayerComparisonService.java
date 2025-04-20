package com.pl.premier_zone.player.comparison;

import com.pl.premier_zone.player.Player;
import com.pl.premier_zone.player.PlayerRepository;
import com.pl.premier_zone.player.statistics.PlayerStatistics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class PlayerComparisonService {

    private final PlayerRepository playerRepository;

    @Autowired
    public PlayerComparisonService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    /**
     * Compare multiple players by their names
     */
    public Map<String, Object> comparePlayersByNames(List<String> playerNames) {
        List<Player> players = playerRepository.findAllById(playerNames);
        
        if (players.size() != playerNames.size()) {
            Set<String> foundPlayerNames = players.stream()
                    .map(Player::getName)
                    .collect(Collectors.toSet());
            
            List<String> notFoundPlayers = playerNames.stream()
                    .filter(name -> !foundPlayerNames.contains(name))
                    .collect(Collectors.toList());
            
            throw new IllegalArgumentException("Players not found: " + String.join(", ", notFoundPlayers));
        }
        
        return generateComparisonData(players);
    }
    
    /**
     * Compare players by position
     */
    public Map<String, Object> comparePlayersByPosition(String position, int limit) {
        List<Player> players = playerRepository.findByPosOrderByXgDesc(position);
        if (limit > 0 && players.size() > limit) {
            players = players.subList(0, limit);
        }
        
        return generateComparisonData(players);
    }
    
    /**
     * Compare players by team
     */
    public Map<String, Object> comparePlayersByTeam(String team, int limit) {
        List<Player> players = playerRepository.findByTeamOrderByMpDesc(team);
        if (limit > 0 && players.size() > limit) {
            players = players.subList(0, limit);
        }
        
        return generateComparisonData(players);
    }
    
    /**
     * Generate comparison data for a list of players
     */
    private Map<String, Object> generateComparisonData(List<Player> players) {
        Map<String, Object> result = new HashMap<>();
        
        // Basic player info
        List<Map<String, Object>> basicInfo = new ArrayList<>();
        for (Player player : players) {
            Map<String, Object> info = new HashMap<>();
            info.put("name", player.getName());
            info.put("team", player.getTeam());
            info.put("position", player.getPos());
            info.put("age", player.getAge());
            info.put("nation", player.getNation());
            basicInfo.add(info);
        }
        result.put("basicInfo", basicInfo);
        
        // Performance metrics
        Map<String, List<Object>> performanceMetrics = new HashMap<>();
        
        // Add standard metrics
        performanceMetrics.put("matchesPlayed", players.stream().map(Player::getMp).collect(Collectors.toList()));
        performanceMetrics.put("starts", players.stream().map(Player::getStarts).collect(Collectors.toList()));
        performanceMetrics.put("minutes", players.stream().map(Player::getMin).collect(Collectors.toList()));
        performanceMetrics.put("goals", players.stream().map(Player::getGls).collect(Collectors.toList()));
        performanceMetrics.put("assists", players.stream().map(Player::getAst).collect(Collectors.toList()));
        performanceMetrics.put("penalties", players.stream().map(Player::getPk).collect(Collectors.toList()));
        performanceMetrics.put("yellowCards", players.stream().map(Player::getCrdy).collect(Collectors.toList()));
        performanceMetrics.put("redCards", players.stream().map(Player::getCrdr).collect(Collectors.toList()));
        performanceMetrics.put("expectedGoals", players.stream().map(Player::getXg).collect(Collectors.toList()));
        performanceMetrics.put("expectedAssists", players.stream().map(Player::getXag).collect(Collectors.toList()));
        
        // Add advanced metrics
        List<Double> goalsPerMinute = new ArrayList<>();
        List<Double> assistsPerMinute = new ArrayList<>();
        List<Double> goalContributions = new ArrayList<>();
        
        for (Player player : players) {
            double minutes = player.getMin();
            double goals = player.getGls();
            double assists = player.getAst();
            
            goalsPerMinute.add(minutes > 0 ? (goals / minutes) * 90 : 0);
            assistsPerMinute.add(minutes > 0 ? (assists / minutes) * 90 : 0);
            goalContributions.add(goals + assists);
        }
        
        performanceMetrics.put("goalsPerNinety", goalsPerMinute);
        performanceMetrics.put("assistsPerNinety", assistsPerMinute);
        performanceMetrics.put("goalContributions", goalContributions);
        
        result.put("performanceMetrics", performanceMetrics);
        
        // Add detailed statistics if available
        List<Map<String, Object>> detailedStats = new ArrayList<>();
        for (Player player : players) {
            Map<String, Object> stats = new HashMap<>();
            PlayerStatistics statistics = player.getStatistics();
            
            if (statistics != null) {
                stats.put("name", player.getName());
                stats.put("gamesPlayed", statistics.getGamesPlayed());
                stats.put("goals", statistics.getGoals());
                stats.put("assists", statistics.getAssists());
                stats.put("yellowCards", statistics.getYellowCards());
                stats.put("redCards", statistics.getRedCards());
                stats.put("minutesPlayed", statistics.getMinutesPlayed());
                detailedStats.add(stats);
            }
        }
        
        if (!detailedStats.isEmpty()) {
            result.put("detailedStatistics", detailedStats);
        }
        
        return result;
    }
} 