package com.pl.premier_zone.rating;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayerRatingRepository extends JpaRepository<PlayerRating, Long> {
    List<PlayerRating> findByPlayerId(String playerId);
    
    @Query("SELECT AVG(r.rating) FROM PlayerRating r WHERE r.player.id = ?1")
    Double getAverageRatingForPlayer(String playerId);
    
    @Query("SELECT r FROM PlayerRating r WHERE r.player.id = ?1 ORDER BY r.createdAt DESC")
    List<PlayerRating> findLatestRatingsForPlayer(String playerId);
    
    @Query("SELECT r FROM PlayerRating r WHERE r.username = ?1 ORDER BY r.createdAt DESC")
    List<PlayerRating> findRatingsByUser(String username);
} 