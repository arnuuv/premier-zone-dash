package com.pl.premier_zone.player;
import jakarta.persistence.Entity;
@Entity
@Table(name = "player_stats")
public class Player {
   private String name;
   private String nation;
   private String pos;
   private Integer age;
   private Integer mp;
   private Integer starts;
   private Double min;
   private Double gls;
   private Double ast;
   private Double pk;
   private Double crdy;
   private Double crdr;
   private Double xg;
   private Double xag;
   private String team;
   
}
