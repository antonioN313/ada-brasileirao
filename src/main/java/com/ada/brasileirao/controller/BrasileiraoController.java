package com.ada.brasileirao.controller;

import com.ada.brasileirao.model.Match;
import com.ada.brasileirao.model.PlayerStats;
import com.ada.brasileirao.model.GoalType;
import com.ada.brasileirao.model.CardType;
import com.ada.brasileirao.repository.CsvReader;
import com.ada.brasileirao.repository.FileCsvReader;
import com.ada.brasileirao.repository.CsvParserImp;
import com.ada.brasileirao.repository.mapper.CsvToDomainMapper;
import com.ada.brasileirao.repository.mapper.MatchMapper;
import com.ada.brasileirao.service.StatisticsService;

import java.nio.file.Path;
import java.util.*;

public class BrasileiraoController {
    private final CsvReader csvReader;
    private final CsvToDomainMapper<Match> matchMapper;

    public BrasileiraoController() {
        this.csvReader = new FileCsvReader(new CsvParserImp());
        this.matchMapper = new MatchMapper();
    }

    public ResultData computeStatistics(Path pathMatches, Path pathEvents) {
        List<Match> matches = csvReader.readAll(pathMatches).stream()
                .map(matchMapper::map)
                .flatMap(Optional::stream)
                .toList();

        Map<String, PlayerStats> stats = new HashMap<>();
        if (pathEvents != null) {
            // Processar arquivo de eventos (pode conter gols E/OU cartões)
            List<Map<String, String>> rawEvents = csvReader.readAll(pathEvents);
            for (Map<String, String> row : rawEvents) {
                processEventRow(row, stats);
            }
        }
        
        // Também tentar processar arquivos separados de gols e cartões
        tryProcessGoalsAndCards(stats);

        StatisticsService service = new StatisticsService(matches, stats);

        ResultData res = new ResultData();
        res.setTeamMostWins2008(service.mostWinsInYear(2008).orElse(null));
        res.setStateFewestMatches(service.stateWithFewestMatches(2003, 2022).orElse(null));
        res.setTopScorer(service.topScorer().map(PlayerStats::getPlayerName).orElse(null));
        res.setTopPenaltyScorer(service.topPenaltyScorer().map(PlayerStats::getPlayerName).orElse(null));
        res.setTopOwnGoalScorer(service.topOwnGoalScorer().map(PlayerStats::getPlayerName).orElse(null));
        res.setTopYellowCards(service.topYellowCards().map(PlayerStats::getPlayerName).orElse(null));
        res.setTopRedCards(service.topRedCards().map(PlayerStats::getPlayerName).orElse(null));
        res.setHighestScoringMatch(service.highestScoringMatch().orElse(null));

        return res;
    }
    
    private void tryProcessGoalsAndCards(Map<String, PlayerStats> stats) {
        try {
            // Processar gols
            Path goalsPath = Path.of("data/campeonato-brasileiro-gols.csv");
            if (java.nio.file.Files.exists(goalsPath)) {
                List<Map<String, String>> goals = csvReader.readAll(goalsPath);
                for (Map<String, String> row : goals) {
                    processEventRow(row, stats);
                }
            }
            
            // Processar cartões
            Path cardsPath = Path.of("data/campeonato-brasileiro-cartoes.csv");
            if (java.nio.file.Files.exists(cardsPath)) {
                List<Map<String, String>> cards = csvReader.readAll(cardsPath);
                for (Map<String, String> row : cards) {
                    processEventRow(row, stats);
                }
            }
        } catch (Exception e) {
            // Silenciosamente ignora se os arquivos não existirem
        }
    }

    private void processEventRow(Map<String, String> row, Map<String, PlayerStats> stats) {
        // Tentar pegar o nome do jogador/atleta
        String jogador = row.get("atleta");
        if (jogador == null || jogador.isBlank()) {
            jogador = row.get("jogador");
        }
        
        if (jogador == null || jogador.isBlank()) {
            return;
        }
        jogador = jogador.trim();
        PlayerStats ps = stats.computeIfAbsent(jogador, PlayerStats::new);

        // Processar gols
        String goalTypeRaw = row.get("tipo_de_gol");
        if (goalTypeRaw == null) {
            goalTypeRaw = row.get("tipo_gol");
        }
        
        if (goalTypeRaw != null && !goalTypeRaw.isBlank()) {
            GoalType gt = GoalType.fromString(goalTypeRaw);
            switch (gt) {
                case PENALTY -> ps.addPenaltyGoals(1);
                case OWN_GOAL -> ps.addOwnGoals(1);
                case NORMAL -> {}
            }
            ps.addGoals(1);
        }

        // Processar cartões
        String cardTypeRaw = row.get("cartao");
        if (cardTypeRaw != null && !cardTypeRaw.isBlank()) {
            CardType ct = CardType.fromString(cardTypeRaw);
            if (ct == CardType.YELLOW) {
                ps.addYellowCards(1);
            } else if (ct == CardType.RED) {
                ps.addRedCards(1);
            }
        }
    }

    public static class ResultData {
        private String teamMostWins2008;
        private String stateFewestMatches;
        private String topScorer;
        private String topPenaltyScorer;
        private String topOwnGoalScorer;
        private String topYellowCards;
        private String topRedCards;
        private Match highestScoringMatch;

        public String getTeamMostWins2008() { return teamMostWins2008; }
        public void setTeamMostWins2008(String t) { this.teamMostWins2008 = t; }

        public String getStateFewestMatches() { return stateFewestMatches; }
        public void setStateFewestMatches(String s) { this.stateFewestMatches = s; }

        public String getTopScorer() { return topScorer; }
        public void setTopScorer(String s) { this.topScorer = s; }

        public String getTopPenaltyScorer() { return topPenaltyScorer; }
        public void setTopPenaltyScorer(String s) { this.topPenaltyScorer = s; }

        public String getTopOwnGoalScorer() { return topOwnGoalScorer; }
        public void setTopOwnGoalScorer(String s) { this.topOwnGoalScorer = s; }

        public String getTopYellowCards() { return topYellowCards; }
        public void setTopYellowCards(String s) { this.topYellowCards = s; }

        public String getTopRedCards() { return topRedCards; }
        public void setTopRedCards(String s) { this.topRedCards = s; }

        public Match getHighestScoringMatch() { return highestScoringMatch; }
        public void setHighestScoringMatch(Match m) { this.highestScoringMatch = m; }
    }
}
