package com.pl.premier_zone.fantasy;

import com.pl.premier_zone.player.Player;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FantasyTeam {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String ownerUsername;
    private String formation;
    private double budget;
    private double spentBudget;
    private int totalPoints;

    private String logoUrl; // New: Team logo image URL

    @ManyToMany
    @JoinTable(
        name = "fantasy_team_players",
        joinColumns = @JoinColumn(name = "team_id"),
        inverseJoinColumns = @JoinColumn(name = "player_id")
    )
    private List<Player> players = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "fantasy_team_captain", joinColumns = @JoinColumn(name = "team_id"))
    private List<String> captainIds = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "fantasy_team_vice_captain", joinColumns = @JoinColumn(name = "team_id"))
    private List<String> viceCaptainIds = new ArrayList<>(); // New: Vice captain support

    @ElementCollection
    @CollectionTable(name = "fantasy_team_weekly_points", joinColumns = @JoinColumn(name = "team_id"))
    @Column(name = "points")
    private List<Integer> weeklyPoints = new ArrayList<>(); // New: Track weekly points
}