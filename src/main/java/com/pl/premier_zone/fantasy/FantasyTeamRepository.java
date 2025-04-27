package com.pl.premier_zone.fantasy;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FantasyTeamRepository extends JpaRepository<FantasyTeam, Long> {
    List<FantasyTeam> findByOwnerUsername(String ownerUsername);
    List<FantasyTeam> findTop10ByOrderByTotalPointsDesc();
} 