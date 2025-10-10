package com.ada.brasileirao.service;

import com.ada.brasileirao.model.Match;
import com.ada.brasileirao.model.PlayerStats;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StatisticsService {
    private final List<Match> matches;
    private final Map<String, PlayerStats> playerStats;

    public StatisticsService(List<Match> matches, Map<String, PlayerStats> playerStats) {
        this.matches = matches;
        this.playerStats = playerStats;
    }

    public Optional<String> mostWinsInYear(int year) {
        return matches.stream()
                .filter(m -> m.getDate().getYear() == year)
                .flatMap(m -> {
                    if (m.getHomeGoals() > m.getAwayGoals()) {
                        return Stream.of(m.getHomeTeam());
                    } else if (m.getAwayGoals() > m.getHomeGoals()) {
                        return Stream.of(m.getAwayTeam());
                    } else {
                        return Stream.empty();
                    }
                })
                .collect(Collectors.groupingBy(team -> team, Collectors.counting()))
                .entrySet().stream()
                .max(Comparator.comparingLong(Map.Entry::getValue))
                .map(Map.Entry::getKey);
    }

    public Optional<String> stateWithFewestMatches(int fromYear, int toYear) {
        return matches.stream()
                .filter(m -> {
                    int y = m.getDate().getYear();
                    return y >= fromYear && y <= toYear;
                })
                .collect(Collectors.groupingBy(Match::getState, Collectors.counting()))
                .entrySet().stream()
                .min(Comparator.comparingLong(Map.Entry::getValue))
                .map(Map.Entry::getKey);
    }

    public Optional<PlayerStats> topScorer() {
        return playerStats.values().stream()
                .max(Comparator.comparingLong(PlayerStats::getGoals));
    }

    public Optional<PlayerStats> topPenaltyScorer() {
        return playerStats.values().stream()
                .max(Comparator.comparingLong(PlayerStats::getPenaltyGoals));
    }

    public Optional<PlayerStats> topOwnGoalScorer() {
        return playerStats.values().stream()
                .max(Comparator.comparingLong(PlayerStats::getOwnGoals));
    }

    public Optional<PlayerStats> topYellowCards() {
        return playerStats.values().stream()
                .max(Comparator.comparingLong(PlayerStats::getYellowCards));
    }

    public Optional<PlayerStats> topRedCards() {
        return playerStats.values().stream()
                .max(Comparator.comparingLong(PlayerStats::getRedCards));
    }

    public Optional<Match> highestScoringMatch() {
        return matches.stream()
                .max(Comparator.comparingInt(Match::totalGoals));
    }
}
