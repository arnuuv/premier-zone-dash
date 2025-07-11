# Premier Zone - Football Analytics Platform

Premier Zone is a comprehensive football analytics platform focused on Premier League statistics, player comparisons, and fantasy football team building.

Project here - https://premierzone.vercel.app/

## Features

### Player Statistics

- Detailed statistics for all Premier League players
- Performance metrics including goals, assists, expected goals (xG), and more
- Advanced filtering and sorting options

### Player Comparison

- Compare multiple players side by side
- Visual representation of statistical differences
- Compare by position, team, or individual selection
- Radar charts for easy performance visualization

### Fantasy Team Builder

- Create your dream team with a £100M budget
- Choose from various formations (4-4-2, 4-3-3, etc.)
- Select players based on real-world statistics
- Calculate fantasy points based on player performances
- Team constraints (max 3 players from same team)
- Captain selection for bonus points

## Technology Stack

- **Backend**: Spring Boot, Java 17
- **Database**: H2 Database (development), PostgreSQL (production)
- **Frontend**: HTML, CSS, JavaScript, Bootstrap 5
- **API**: RESTful API architecture

## Getting Started

### Prerequisites

- Java 17 or higher
- Maven 3.6 or higher

### Installation

1. Clone the repository

```
git clone https://github.com/yourusername/premier-zone.git
cd premier-zone
```

2. Build the application

```
./mvnw clean package
```

3. Run the application

```
./mvnw spring-boot:run
```

4. Access the application

- Open your browser and navigate to `http://localhost:8080`

### API Endpoints

#### Players

- `GET /api/players` - Get all players
- `GET /api/players/{id}` - Get player by ID
- `GET /api/players/team/{team}` - Get players by team

#### Player Comparison

- `POST /api/player-comparison/by-names` - Compare players by names
- `GET /api/player-comparison/by-position/{position}` - Compare players by position
- `GET /api/player-comparison/by-team/{team}` - Compare players by team

#### Fantasy Teams

- `GET /api/fantasy/teams` - Get all fantasy teams
- `GET /api/fantasy/teams/{id}` - Get fantasy team by ID
- `POST /api/fantasy/teams` - Create a new fantasy team
- `POST /api/fantasy/teams/{teamId}/players/{playerId}` - Add player to team
- `DELETE /api/fantasy/teams/{teamId}/players/{playerId}` - Remove player from team
- `POST /api/fantasy/teams/{teamId}/captain/{playerId}` - Set team captain
- `GET /api/fantasy/teams/{teamId}/points` - Calculate team points

## Database Schema

### Player

- id (String): Unique identifier
- name (String): Player name
- team (String): Current team
- pos (String): Position (GK, DF, MF, FW)
- age (Integer): Player age
- nation (String): Nationality
- mp (Integer): Matches played
- starts (Integer): Matches started
- min (Integer): Minutes played
- gls (Integer): Goals scored
- ast (Integer): Assists
- pk (Integer): Penalties taken
- crdy (Integer): Yellow cards
- crdr (Integer): Red cards
- xg (Double): Expected goals
- xag (Double): Expected assists
- marketValue (Double): Market value in millions
- cleanSheets (Integer): Clean sheets (GK/DF)
- penaltySaves (Integer): Penalties saved (GK)
- penaltyMisses (Integer): Penalties missed

### FantasyTeam

- id (Long): Unique identifier
- name (String): Team name
- ownerUsername (String): Team owner
- formation (String): Team formation
- budget (Double): Total budget
- spentBudget (Double): Amount spent
- totalPoints (Integer): Total fantasy points
- players (List<Player>): Team players
- captainIds (List<String>): Team captains

## Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Acknowledgments

- Data sourced from public Premier League statistics
- Inspired by Fantasy Premier League and other football analytics platforms
- This project was inspired by SWErikCodes and his YouTube tutorial: Ultimate Java Backend Project: Spring Boot Tutorial.



