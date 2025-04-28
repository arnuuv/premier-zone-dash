package com.pl.premier_zone.rating;

import java.util.List;
import java.util.Map;

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
@RequestMapping("/api/ratings")
public class PlayerRatingController {

    private final PlayerRatingService playerRatingService;

    @Autowired
    public PlayerRatingController(PlayerRatingService playerRatingService) {
        this.playerRatingService = playerRatingService;
    }

    @GetMapping("/player/{playerId}")
    public ResponseEntity<List<PlayerRating>> getPlayerRatings(@PathVariable String playerId) {
        return ResponseEntity.ok(playerRatingService.getPlayerRatings(playerId));
    }

    @GetMapping("/player/{playerId}/average")
    public ResponseEntity<Map<String, Double>> getAverageRating(@PathVariable String playerId) {
        Double averageRating = playerRatingService.getAverageRating(playerId);
        return ResponseEntity.ok(Map.of("averageRating", averageRating != null ? averageRating : 0.0));
    }

    @PostMapping("/player/{playerId}")
    public ResponseEntity<PlayerRating> ratePlayer(
            @PathVariable String playerId,
            @RequestBody Map<String, Object> request) {
        
        String username = (String) request.get("username");
        Integer rating = (Integer) request.get("rating");
        String comment = (String) request.get("comment");
        
        if (username == null || rating == null) {
            return ResponseEntity.badRequest().build();
        }
        
        try {
            PlayerRating playerRating = playerRatingService.ratePlayer(playerId, username, rating, comment);
            return ResponseEntity.status(HttpStatus.CREATED).body(playerRating);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{ratingId}")
    public ResponseEntity<PlayerRating> updateRating(
            @PathVariable Long ratingId,
            @RequestBody Map<String, Object> request) {
        
        Integer rating = (Integer) request.get("rating");
        String comment = (String) request.get("comment");
        
        try {
            return playerRatingService.updateRating(ratingId, rating, comment)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{ratingId}")
    public ResponseEntity<Void> deleteRating(@PathVariable Long ratingId) {
        playerRatingService.deleteRating(ratingId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/user/{username}")
    public ResponseEntity<List<PlayerRating>> getUserRatings(@PathVariable String username) {
        return ResponseEntity.ok(playerRatingService.getUserRatings(username));
    }
} 