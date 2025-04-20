package com.pl.premier_zone.player.comparison;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/player-comparison")
public class PlayerComparisonController {

    private final PlayerComparisonService playerComparisonService;

    @Autowired
    public PlayerComparisonController(PlayerComparisonService playerComparisonService) {
        this.playerComparisonService = playerComparisonService;
    }

    @PostMapping("/by-names")
    public ResponseEntity<?> comparePlayersByNames(@RequestBody List<String> playerNames) {
        try {
            if (playerNames.size() < 2) {
                return ResponseEntity.badRequest().body("At least two players are required for comparison");
            }
            
            Map<String, Object> comparisonData = playerComparisonService.comparePlayersByNames(playerNames);
            return ResponseEntity.ok(comparisonData);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("An error occurred during comparison: " + e.getMessage());
        }
    }

    @GetMapping("/by-position/{position}")
    public ResponseEntity<?> comparePlayersByPosition(
            @PathVariable String position,
            @RequestParam(defaultValue = "5") int limit) {
        try {
            Map<String, Object> comparisonData = playerComparisonService.comparePlayersByPosition(position, limit);
            return ResponseEntity.ok(comparisonData);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("An error occurred during comparison: " + e.getMessage());
        }
    }

    @GetMapping("/by-team/{team}")
    public ResponseEntity<?> comparePlayersByTeam(
            @PathVariable String team,
            @RequestParam(defaultValue = "5") int limit) {
        try {
            Map<String, Object> comparisonData = playerComparisonService.comparePlayersByTeam(team, limit);
            return ResponseEntity.ok(comparisonData);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("An error occurred during comparison: " + e.getMessage());
        }
    }
} 