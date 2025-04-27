package com.pl.premier_zone.fantasy;

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
@RequestMapping("/api/fantasy")
public class FantasyTeamController {

    private final FantasyTeamService fantasyTeamService;

    @Autowired
    public FantasyTeamController(FantasyTeamService fantasyTeamService) {
        this.fantasyTeamService = fantasyTeamService;
    }

    @GetMapping("/teams")
    public ResponseEntity<List<FantasyTeam>> getAllFantasyTeams() {
        return ResponseEntity.ok(fantasyTeamService.getAllFantasyTeams());
    }

    @GetMapping("/teams/{id}")
    public ResponseEntity<FantasyTeam> getFantasyTeamById(@PathVariable Long id) {
        return fantasyTeamService.getFantasyTeamById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/teams/user/{username}")
    public ResponseEntity<List<FantasyTeam>> getFantasyTeamsByOwner(@PathVariable String username) {
        return ResponseEntity.ok(fantasyTeamService.getFantasyTeamsByOwner(username));
    }

    @GetMapping("/teams/top")
    public ResponseEntity<List<FantasyTeam>> getTopFantasyTeams() {
        return ResponseEntity.ok(fantasyTeamService.getTopFantasyTeams());
    }

    @PostMapping("/teams")
    public ResponseEntity<?> createFantasyTeam(@RequestBody Map<String, String> request) {
        try {
            String name = request.get("name");
            String ownerUsername = request.get("ownerUsername");
            String formation = request.get("formation");
            
            if (name == null || ownerUsername == null || formation == null) {
                return ResponseEntity.badRequest().body("Name, owner username, and formation are required");
            }
            
            FantasyTeam team = fantasyTeamService.createFantasyTeam(name, ownerUsername, formation);
            return ResponseEntity.status(HttpStatus.CREATED).body(team);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/teams/{teamId}/players/{playerId}")
    public ResponseEntity<?> addPlayerToTeam(@PathVariable Long teamId, @PathVariable String playerId) {
        try {
            FantasyTeam team = fantasyTeamService.addPlayerToTeam(teamId, playerId);
            return ResponseEntity.ok(team);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/teams/{teamId}/players/{playerId}")
    public ResponseEntity<?> removePlayerFromTeam(@PathVariable Long teamId, @PathVariable String playerId) {
        try {
            FantasyTeam team = fantasyTeamService.removePlayerFromTeam(teamId, playerId);
            return ResponseEntity.ok(team);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/teams/{teamId}/captain/{playerId}")
    public ResponseEntity<?> setCaptain(@PathVariable Long teamId, @PathVariable String playerId) {
        try {
            FantasyTeam team = fantasyTeamService.setCaptain(teamId, playerId);
            return ResponseEntity.ok(team);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/teams/{teamId}/formation")
    public ResponseEntity<?> updateTeamFormation(@PathVariable Long teamId, @RequestBody Map<String, String> request) {
        try {
            String formation = request.get("formation");
            if (formation == null) {
                return ResponseEntity.badRequest().body("Formation is required");
            }
            
            FantasyTeam team = fantasyTeamService.updateTeamFormation(teamId, formation);
            return ResponseEntity.ok(team);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/teams/{teamId}/points")
    public ResponseEntity<Map<String, Integer>> calculateTeamPoints(@PathVariable Long teamId) {
        try {
            int points = fantasyTeamService.calculateTeamPoints(teamId);
            return ResponseEntity.ok(Map.of("points", points));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/teams/{teamId}")
    public ResponseEntity<Void> deleteFantasyTeam(@PathVariable Long teamId) {
        try {
            fantasyTeamService.deleteFantasyTeam(teamId);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
} 