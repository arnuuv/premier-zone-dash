package com.pl.premier_zone.prediction;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MatchRepository extends JpaRepository<Match, Long> {
    
    List<Match> findByCompletedFalseOrderByMatchDateAsc();
    
    List<Match> findByCompletedTrueOrderByMatchDateDesc();
    
    List<Match> findByHomeTeamOrAwayTeamOrderByMatchDateDesc(String homeTeam, String awayTeam);
    
    List<Match> findByMatchDateBetween(LocalDateTime start, LocalDateTime end);
    
    @Query("SELECT m FROM Match m WHERE (m.homeTeam = ?1 AND m.awayTeam = ?2) OR (m.homeTeam = ?2 AND m.awayTeam = ?1) ORDER BY m.matchDate DESC")
    List<Match> findHeadToHeadMatches(String team1, String team2);
    
    @Query("SELECT m FROM Match m WHERE m.homeTeam = ?1 OR m.awayTeam = ?1 ORDER BY m.matchDate DESC")
    List<Match> findTeamMatches(String team);
} 