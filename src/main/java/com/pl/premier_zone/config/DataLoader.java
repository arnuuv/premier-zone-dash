package com.pl.premier_zone.config;

import com.pl.premier_zone.player.Player;
import com.pl.premier_zone.player.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class DataLoader implements CommandLineRunner {

    private final PlayerRepository playerRepository;

    @Autowired
    public DataLoader(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    @Override
    public void run(String... args) {
        // Check if data already exists
        if (playerRepository.count() > 0) {
            return;
        }

        // Sample players data
        List<Player> players = Arrays.asList(
            createPlayer("erling-haaland", "Erling Haaland", "Manchester City", "FW", 23, "Norway", 38, 36, 3420, 36, 8, 7, 2, 0, 27.5, 5.2, 90.0),
            createPlayer("mohamed-salah", "Mohamed Salah", "Liverpool", "FW", 31, "Egypt", 37, 36, 3240, 24, 13, 5, 4, 0, 22.3, 10.8, 75.0),
            createPlayer("bukayo-saka", "Bukayo Saka", "Arsenal", "FW", 22, "England", 38, 37, 3330, 15, 11, 2, 5, 0, 14.2, 9.7, 85.0),
            createPlayer("kevin-de-bruyne", "Kevin De Bruyne", "Manchester City", "MF", 32, "Belgium", 32, 28, 2520, 4, 16, 0, 3, 0, 5.8, 14.3, 70.0),
            createPlayer("bruno-fernandes", "Bruno Fernandes", "Manchester United", "MF", 29, "Portugal", 37, 37, 3330, 8, 8, 2, 7, 0, 9.2, 7.5, 65.0),
            createPlayer("martin-odegaard", "Martin Ødegaard", "Arsenal", "MF", 25, "Norway", 36, 36, 3240, 15, 7, 0, 4, 0, 12.8, 6.9, 80.0),
            createPlayer("rodri", "Rodri", "Manchester City", "MF", 27, "Spain", 34, 34, 3060, 8, 7, 0, 8, 0, 6.2, 5.8, 85.0),
            createPlayer("virgil-van-dijk", "Virgil van Dijk", "Liverpool", "DF", 32, "Netherlands", 35, 35, 3150, 3, 1, 0, 2, 1, 3.2, 0.8, 60.0),
            createPlayer("william-saliba", "William Saliba", "Arsenal", "DF", 22, "France", 37, 37, 3330, 3, 1, 0, 3, 0, 2.8, 0.7, 75.0),
            createPlayer("trent-alexander-arnold", "Trent Alexander-Arnold", "Liverpool", "DF", 25, "England", 34, 32, 2880, 3, 9, 0, 4, 0, 2.5, 8.7, 65.0),
            createPlayer("alisson", "Alisson", "Liverpool", "GK", 31, "Brazil", 37, 37, 3330, 0, 0, 0, 1, 0, 0.0, 0.0, 55.0),
            createPlayer("ederson", "Ederson", "Manchester City", "GK", 30, "Brazil", 35, 35, 3150, 0, 0, 0, 2, 0, 0.0, 0.0, 50.0),
            createPlayer("david-raya", "David Raya", "Arsenal", "GK", 28, "Spain", 32, 32, 2880, 0, 0, 0, 1, 0, 0.0, 0.0, 45.0),
            createPlayer("son-heung-min", "Son Heung-min", "Tottenham", "FW", 31, "South Korea", 36, 35, 3150, 17, 10, 1, 3, 0, 15.8, 8.9, 60.0),
            createPlayer("cole-palmer", "Cole Palmer", "Chelsea", "FW", 21, "England", 34, 30, 2700, 22, 11, 6, 5, 0, 18.5, 9.2, 70.0),
            createPlayer("declan-rice", "Declan Rice", "Arsenal", "MF", 25, "England", 37, 37, 3330, 7, 8, 0, 6, 0, 6.3, 7.1, 85.0),
            createPlayer("phil-foden", "Phil Foden", "Manchester City", "MF", 23, "England", 35, 32, 2880, 19, 8, 0, 3, 0, 17.2, 7.3, 90.0),
            createPlayer("ruben-dias", "Rúben Dias", "Manchester City", "DF", 27, "Portugal", 33, 32, 2880, 1, 2, 0, 5, 0, 1.2, 1.8, 65.0),
            createPlayer("gabriel", "Gabriel", "Arsenal", "DF", 26, "Brazil", 38, 38, 3420, 5, 1, 0, 7, 0, 4.8, 0.9, 60.0),
            createPlayer("jordan-pickford", "Jordan Pickford", "Everton", "GK", 29, "England", 38, 38, 3420, 0, 0, 0, 3, 0, 0.0, 0.0, 40.0)
        );

        playerRepository.saveAll(players);
    }

    private Player createPlayer(String id, String name, String team, String pos, int age, String nation, 
                               int mp, int starts, int min, int gls, int ast, int pk, int crdy, int crdr, 
                               double xg, double xag, double marketValue) {
        Player player = new Player();
        player.setId(id);
        player.setName(name);
        player.setTeam(team);
        player.setPos(pos);
        player.setAge(age);
        player.setNation(nation);
        player.setMp(mp);
        player.setStarts(starts);
        player.setMin(min);
        player.setGls(gls);
        player.setAst(ast);
        player.setPk(pk);
        player.setCrdy(crdy);
        player.setCrdr(crdr);
        player.setXg(xg);
        player.setXag(xag);
        player.setMarketValue(marketValue);
        
        // Set additional fantasy-related fields
        if (pos.equals("GK")) {
            player.setCleanSheets((int)(mp * 0.4)); // Approximately 40% of matches are clean sheets
            player.setPenaltySaves((int)(mp * 0.05)); // Save about 5% of penalties faced
        } else {
            player.setCleanSheets(0);
            player.setPenaltySaves(0);
        }
        
        player.setPenaltyMisses(pos.equals("FW") ? (int)(pk * 0.1) : 0); // 10% of penalties are missed by forwards
        
        return player;
    }
} 