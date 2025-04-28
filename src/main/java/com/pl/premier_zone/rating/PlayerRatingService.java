package com.pl.premier_zone.rating;

import com.pl.premier_zone.player.Player;
import com.pl.premier_zone.player.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class PlayerRatingService {

    private final PlayerRatingRepository playerRatingRepository;
    private final PlayerRepository playerRepository;

    @Autowired
    public PlayerRatingService(PlayerRatingRepository playerRatingRepository, PlayerRepository playerRepository) {
        this.playerRatingRepository = playerRatingRepository;
        this.playerRepository = playerRepository;
    }

    public List<PlayerRating> getPlayerRatings(String playerId) {
        return playerRatingRepository.findByPlayerId(playerId);
    }

    public Double getAverageRating(String playerId) {
        return playerRatingRepository.getAverageRatingForPlayer(playerId);
    }

    @Transactional
    public PlayerRating ratePlayer(String playerId, String username, Integer rating, String comment) {
        if (rating < 1 || rating > 10) {
            throw new IllegalArgumentException("Rating must be between 1 and 10");
        }

        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new IllegalArgumentException("Player not found with ID: " + playerId));

        PlayerRating playerRating = new PlayerRating();
        playerRating.setPlayer(player);
        playerRating.setUsername(username);
        playerRating.setRating(rating);
        playerRating.setComment(comment);

        return playerRatingRepository.save(playerRating);
    }

    @Transactional
    public Optional<PlayerRating> updateRating(Long ratingId, Integer newRating, String newComment) {
        if (newRating != null && (newRating < 1 || newRating > 10)) {
            throw new IllegalArgumentException("Rating must be between 1 and 10");
        }

        return playerRatingRepository.findById(ratingId)
                .map(rating -> {
                    if (newRating != null) {
                        rating.setRating(newRating);
                    }
                    if (newComment != null) {
                        rating.setComment(newComment);
                    }
                    return playerRatingRepository.save(rating);
                });
    }

    @Transactional
    public void deleteRating(Long ratingId) {
        playerRatingRepository.deleteById(ratingId);
    }

    public List<PlayerRating> getUserRatings(String username) {
        return playerRatingRepository.findRatingsByUser(username);
    }
} 