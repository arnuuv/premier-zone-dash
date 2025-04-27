package com.pl.premier_zone.fantasy;

import com.pl.premier_zone.player.Player;
import com.pl.premier_zone.player.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class FantasyTeamService {

    private final FantasyTeamRepository fantasyTeamRepository;
    private final PlayerRepository playerRepository;
    
    // Default budget for new teams (in millions)
    private static final double DEFAULT_BUDGET = 100.0;
    
    // Maximum players allowed from the same team
    private static final int MAX_PLAYERS_FROM_SAME_TEAM = 3;
    
    // Valid formations
    private static final Set<String> VALID_FORMATIONS = Set.of(
            "4-4-2", "4-3-3", "3-5-2", "5-3-2", "3-4-3", "4-5-1", "5-4-1");

    @Autowired
    public FantasyTeamService(FantasyTeamRepository fantasyTeamRepository, PlayerRepository playerRepository) {
        this.fantasyTeamRepository = fantasyTeamRepository;
        this.playerRepository = playerRepository;
    }

    public List<FantasyTeam> getAllFantasyTeams() {
        return fantasyTeamRepository.findAll();
    }

    public Optional<FantasyTeam> getFantasyTeamById(Long id) {
        return fantasyTeamRepository.findById(id);
    }

    public List<FantasyTeam> getFantasyTeamsByOwner(String ownerUsername) {
        return fantasyTeamRepository.findByOwnerUsername(ownerUsername);
    }

    public List<FantasyTeam> getTopFantasyTeams() {
        return fantasyTeamRepository.findTop10ByOrderByTotalPointsDesc();
    }

    @Transactional
    public FantasyTeam createFantasyTeam(String name, String ownerUsername, String formation) {
        if (!VALID_FORMATIONS.contains(formation)) {
            throw new IllegalArgumentException("Invalid formation. Valid formations are: " + 
                    String.join(", ", VALID_FORMATIONS));
        }
        
        FantasyTeam team = new FantasyTeam();
        team.setName(name);
        team.setOwnerUsername(ownerUsername);
        team.setFormation(formation);
        team.setBudget(DEFAULT_BUDGET);
        team.setSpentBudget(0.0);
        team.setTotalPoints(0);
        
        return fantasyTeamRepository.save(team);
    }

    @Transactional
    public FantasyTeam addPlayerToTeam(Long teamId, String playerId) {
        FantasyTeam team = fantasyTeamRepository.findById(teamId)
                .orElseThrow(() -> new IllegalArgumentException("Fantasy team not found with ID: " + teamId));
        
        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new IllegalArgumentException("Player not found with ID: " + playerId));
        
        // Check if team already has 11 players
        if (team.getPlayers().size() >= 11) {
            throw new IllegalStateException("Team already has the maximum of 11 players");
        }
        
        // Check if player is already in the team
        if (team.getPlayers().stream().anyMatch(p -> p.getName().equals(player.getName()))) {
            throw new IllegalStateException("Player is already in the team");
        }
        
        // Check formation constraints
        if (!isPlayerPositionAllowedInFormation(player.getPos(), team.getFormation(), team.getPlayers())) {
            throw new IllegalStateException("Cannot add more players in position " + player.getPos() + 
                    " with formation " + team.getFormation());
        }
        
        // Check team constraints (max 3 players from same team)
        Map<String, Long> teamCounts = team.getPlayers().stream()
                .collect(Collectors.groupingBy(Player::getTeam, Collectors.counting()));
        
        if (teamCounts.getOrDefault(player.getTeam(), 0L) >= MAX_PLAYERS_FROM_SAME_TEAM) {
            throw new IllegalStateException("Cannot have more than " + MAX_PLAYERS_FROM_SAME_TEAM + 
                    " players from the same team");
        }
        
        // Check budget constraints
        double playerValue = player.getMarketValue() != null ? player.getMarketValue() : 5.0; // Default value if not set
        if (team.getSpentBudget() + playerValue > team.getBudget()) {
            throw new IllegalStateException("Not enough budget to add this player");
        }
        
        // Add player to team
        team.getPlayers().add(player);
        team.setSpentBudget(team.getSpentBudget() + playerValue);
        
        return fantasyTeamRepository.save(team);
    }

    @Transactional
    public FantasyTeam removePlayerFromTeam(Long teamId, String playerId) {
        FantasyTeam team = fantasyTeamRepository.findById(teamId)
                .orElseThrow(() -> new IllegalArgumentException("Fantasy team not found with ID: " + teamId));
        
        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new IllegalArgumentException("Player not found with ID: " + playerId));
        
        // Check if player is in the team
        if (team.getPlayers().stream().noneMatch(p -> p.getName().equals(player.getName()))) {
            throw new IllegalStateException("Player is not in the team");
        }
        
        // Remove player from team
        team.getPlayers().removeIf(p -> p.getName().equals(player.getName()));
        
        // Remove from captain if set
        team.getCaptainIds().remove(player.getName());
        
        // Update spent budget
        double playerValue = player.getMarketValue() != null ? player.getMarketValue() : 5.0;
        team.setSpentBudget(team.getSpentBudget() - playerValue);
        
        return fantasyTeamRepository.save(team);
    }

    @Transactional
    public FantasyTeam setCaptain(Long teamId, String playerId) {
        FantasyTeam team = fantasyTeamRepository.findById(teamId)
                .orElseThrow(() -> new IllegalArgumentException("Fantasy team not found with ID: " + teamId));
        
        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new IllegalArgumentException("Player not found with ID: " + playerId));
        
        // Check if player is in the team
        if (team.getPlayers().stream().noneMatch(p -> p.getName().equals(player.getName()))) {
            throw new IllegalStateException("Player is not in the team");
        }
        
        // Clear existing captain and set new one
        team.getCaptainIds().clear();
        team.getCaptainIds().add(player.getName());
        
        return fantasyTeamRepository.save(team);
    }

    @Transactional
    public FantasyTeam updateTeamFormation(Long teamId, String newFormation) {
        if (!VALID_FORMATIONS.contains(newFormation)) {
            throw new IllegalArgumentException("Invalid formation. Valid formations are: " + 
                    String.join(", ", VALID_FORMATIONS));
        }
        
        FantasyTeam team = fantasyTeamRepository.findById(teamId)
                .orElseThrow(() -> new IllegalArgumentException("Fantasy team not found with ID: " + teamId));
        
        // Check if current players fit in the new formation
        if (!isFormationValidWithCurrentPlayers(team.getPlayers(), newFormation)) {
            throw new IllegalStateException("Current players don't fit in the new formation");
        }
        
        team.setFormation(newFormation);
        return fantasyTeamRepository.save(team);
    }

    @Transactional
    public void deleteFantasyTeam(Long teamId) {
        fantasyTeamRepository.deleteById(teamId);
    }
    
    // Helper method to check if a player's position is allowed in the formation
    private boolean isPlayerPositionAllowedInFormation(String position, String formation, List<Player> currentPlayers) {
        // Count current players by position
        Map<String, Long> positionCounts = currentPlayers.stream()
                .collect(Collectors.groupingBy(Player::getPos, Collectors.counting()));
        
        // Parse formation (e.g., "4-3-3" -> [4, 3, 3])
        String[] parts = formation.split("-");
        int defenders = Integer.parseInt(parts[0]);
        int midfielders = Integer.parseInt(parts[1]);
        int forwards = Integer.parseInt(parts[2]);
        
        // Check based on position
        switch (position) {
            case "GK":
                return positionCounts.getOrDefault("GK", 0L) < 1; // Max 1 goalkeeper
            case "DF":
                return positionCounts.getOrDefault("DF", 0L) < defenders;
            case "MF":
                return positionCounts.getOrDefault("MF", 0L) < midfielders;
            case "FW":
                return positionCounts.getOrDefault("FW", 0L) < forwards;
            default:
                return false;
        }
    }
    
    // Helper method to check if a formation is valid with the current players
    private boolean isFormationValidWithCurrentPlayers(List<Player> players, String formation) {
        // Count players by position
        Map<String, Long> positionCounts = players.stream()
                .collect(Collectors.groupingBy(Player::getPos, Collectors.counting()));
        
        // Parse formation
        String[] parts = formation.split("-");
        int defenders = Integer.parseInt(parts[0]);
        int midfielders = Integer.parseInt(parts[1]);
        int forwards = Integer.parseInt(parts[2]);
        
        // Check if current players fit in the new formation
        return positionCounts.getOrDefault("GK", 0L) <= 1 &&
               positionCounts.getOrDefault("DF", 0L) <= defenders &&
               positionCounts.getOrDefault("MF", 0L) <= midfielders &&
               positionCounts.getOrDefault("FW", 0L) <= forwards;
    }
    
    // Calculate team points based on player performances
    @Transactional
    public int calculateTeamPoints(Long teamId) {
        FantasyTeam team = fantasyTeamRepository.findById(teamId)
                .orElseThrow(() -> new IllegalArgumentException("Fantasy team not found with ID: " + teamId));
        
        int totalPoints = 0;
        
        for (Player player : team.getPlayers()) {
            int playerPoints = calculatePlayerPoints(player);
            
            // Double points for captain
            if (team.getCaptainIds().contains(player.getName())) {
                playerPoints *= 2;
            }
            
            totalPoints += playerPoints;
        }
        
        team.setTotalPoints(totalPoints);
        fantasyTeamRepository.save(team);
        
        return totalPoints;
    }
    
    // Calculate points for a single player based on their statistics
    private int calculatePlayerPoints(Player player) {
        int points = 0;
        
        // Points for playing
        if (player.getMin() > 0) {
            points += 2;
        }
        
        // Points for goals (position-dependent)
        int goalPoints;
        switch (player.getPos()) {
            case "GK":
            case "DF":
                goalPoints = 6;
                break;
            case "MF":
                goalPoints = 5;
                break;
            case "FW":
                goalPoints = 4;
                break;
            default:
                goalPoints = 4;
        }
        
        points += player.getGls() * goalPoints;
        
        // Points for assists
        points += player.getAst() * 3;
        
        // Clean sheet points (only for GK and DF who played at least 60 minutes)
        if ((player.getPos().equals("GK") || player.getPos().equals("DF")) && 
            player.getMin() >= 60 && player.getCleanSheets() > 0) {
            points += player.getCleanSheets() * 4;
        }
        
        // Penalty saves (GK only)
        if (player.getPos().equals("GK") && player.getPenaltySaves() > 0) {
            points += player.getPenaltySaves() * 5;
        }
        
        // Penalty misses
        points -= player.getPenaltyMisses() * 2;
        
        // Cards
        points -= player.getCrdy() * 1;  // Yellow cards
        points -= player.getCrdr() * 3;  // Red cards
        
        return points;
    }
} 