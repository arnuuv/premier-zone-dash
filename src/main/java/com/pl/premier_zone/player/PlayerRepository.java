package com.pl.premier_zone.player;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayerRepository extends JpaRepository<Player, String> {
    void deleteByName(String playerName);

    Optional<Player> findByName(String name);
    
    // Add statistics-related queries
    @Query("SELECT p FROM Player p JOIN p.statistics s ORDER BY s.goals DESC")
    List<Player> findAllOrderByGoalsDesc();
    
    @Query("SELECT p FROM Player p JOIN p.statistics s ORDER BY s.assists DESC")
    List<Player> findAllOrderByAssistsDesc();
    
    // Add comparison-related queries
    List<Player> findByPosOrderByXgDesc(String position);
    
    List<Player> findByTeamOrderByMpDesc(String team);
}