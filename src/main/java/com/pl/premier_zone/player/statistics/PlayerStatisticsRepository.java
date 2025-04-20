package com.pl.premier_zone.player.statistics;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayerStatisticsRepository extends JpaRepository<PlayerStatistics, Long> {
    Optional<PlayerStatistics> findByPlayerId(String playerId);
    Optional<PlayerStatistics> findByPlayerName(String playerName);
} 