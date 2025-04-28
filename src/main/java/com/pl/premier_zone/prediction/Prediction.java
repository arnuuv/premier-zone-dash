package com.pl.premier_zone.prediction;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Prediction {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "match_id")
    private Match match;
    
    private String username;
    private Integer predictedHomeScore;
    private Integer predictedAwayScore;
    private String predictedOutcome; // "HOME_WIN", "AWAY_WIN", "DRAW"
    private Integer points; // Points earned for correct prediction
    private LocalDateTime createdAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
    
    public void calculateOutcome() {
        if (predictedHomeScore > predictedAwayScore) {
            predictedOutcome = "HOME_WIN";
        } else if (predictedHomeScore < predictedAwayScore) {
            predictedOutcome = "AWAY_WIN";
        } else {
            predictedOutcome = "DRAW";
        }
    }
    
    public void calculatePoints() {
        if (match.getCompleted()) {
            String actualOutcome;
            if (match.getHomeScore() > match.getAwayScore()) {
                actualOutcome = "HOME_WIN";
            } else if (match.getHomeScore() < match.getAwayScore()) {
                actualOutcome = "AWAY_WIN";
            } else {
                actualOutcome = "DRAW";
            }
            
            // 3 points for correct outcome
            if (predictedOutcome.equals(actualOutcome)) {
                points = 3;
                
                // Additional 2 points for correct score
                if (predictedHomeScore.equals(match.getHomeScore()) && 
                    predictedAwayScore.equals(match.getAwayScore())) {
                    points += 2;
                }
            } else {
                points = 0;
            }
        }
    }
} 