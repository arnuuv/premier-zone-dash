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
public class Match {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String homeTeam;
    private String awayTeam;
    private LocalDateTime matchDate;
    private Integer homeScore;
    private Integer awayScore;
    private Boolean completed;
    private String season;
    private String competition;
    private String venue;
    
    // Match statistics
    private Integer homePossession;
    private Integer awayPossession;
    private Integer homeShots;
    private Integer awayShots;
    private Integer homeShotsOnTarget;
    private Integer awayShotsOnTarget;
    private Integer homeCorners;
    private Integer awayCorners;
    private Integer homeFouls;
    private Integer awayFouls;
    private Integer homeYellowCards;
    private Integer awayYellowCards;
    private Integer homeRedCards;
    private Integer awayRedCards;
} 