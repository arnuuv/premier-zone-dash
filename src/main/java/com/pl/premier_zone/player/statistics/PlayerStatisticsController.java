package com.pl.premier_zone.player.statistics;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/player-statistics")
public class PlayerStatisticsController {

    private final PlayerStatisticsService playerStatisticsService;

    @Autowired
    public PlayerStatisticsController(PlayerStatisticsService playerStatisticsService) {
        this.playerStatisticsService = playerStatisticsService;
    }

    @GetMapping
    public ResponseEntity<List<PlayerStatistics>> getAllPlayerStatistics() {
        return ResponseEntity.ok(playerStatisticsService.getAllPlayerStatistics());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlayerStatistics> getPlayerStatisticsById(@PathVariable Long id) {
        return playerStatisticsService.getPlayerStatisticsById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/player/{playerId}")
    public ResponseEntity<PlayerStatistics> getPlayerStatisticsByPlayerId(@PathVariable String playerId) {
        return playerStatisticsService.getPlayerStatisticsByPlayerId(playerId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/player/name/{playerName}")
    public ResponseEntity<PlayerStatistics> getPlayerStatisticsByPlayerName(@PathVariable String playerName) {
        return playerStatisticsService.getPlayerStatisticsByPlayerName(playerName)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/player/{playerId}")
    public ResponseEntity<PlayerStatistics> createPlayerStatistics(
            @PathVariable String playerId,
            @RequestBody PlayerStatistics statistics) {
        try {
            PlayerStatistics createdStatistics = playerStatisticsService.createPlayerStatistics(playerId, statistics);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdStatistics);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<PlayerStatistics> updatePlayerStatistics(
            @PathVariable Long id,
            @RequestBody PlayerStatistics statistics) {
        try {
            PlayerStatistics updatedStatistics = playerStatisticsService.updatePlayerStatistics(id, statistics);
            return ResponseEntity.ok(updatedStatistics);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePlayerStatistics(@PathVariable Long id) {
        playerStatisticsService.deletePlayerStatistics(id);
        return ResponseEntity.noContent().build();
    }
} 