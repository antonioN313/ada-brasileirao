package com.ada.brasileirao.service;

import com.ada.brasileirao.model.Match;
import com.ada.brasileirao.model.PlayerStats;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class StatisticsServiceTest {

    private List<Match> matches;
    private Map<String, PlayerStats> playerStats;
    private StatisticsService service;

    @BeforeEach
    void setUp() {
        // Criar dados de teste
        matches = new ArrayList<>();
        matches.add(new Match(
            LocalDate.of(2008, 5, 10),
            "São Paulo", "Corinthians",
            3, 1, "SP"
        ));
        matches.add(new Match(
            LocalDate.of(2008, 6, 15),
            "São Paulo", "Palmeiras",
            2, 0, "SP"
        ));
        matches.add(new Match(
            LocalDate.of(2008, 7, 20),
            "Flamengo", "Vasco",
            1, 1, "RJ"
        ));

        playerStats = new HashMap<>();
        
        PlayerStats fred = new PlayerStats("Fred");
        fred.addGoals(30);
        fred.addPenaltyGoals(5);
        playerStats.put("Fred", fred);

        PlayerStats neymar = new PlayerStats("Neymar");
        neymar.addGoals(25);
        neymar.addYellowCards(8);
        playerStats.put("Neymar", neymar);

        service = new StatisticsService(matches, playerStats);
    }

    @Test
    void testMostWinsInYear() {
        Optional<String> result = service.mostWinsInYear(2008);
        assertTrue(result.isPresent());
        assertEquals("São Paulo", result.get());
    }

    @Test
    void testStateWithFewestMatches() {
        Optional<String> result = service.stateWithFewestMatches(2003, 2022);
        assertTrue(result.isPresent());
        assertTrue(result.get().equals("SP") || result.get().equals("RJ"));
    }

    @Test
    void testTopScorer() {
        Optional<PlayerStats> result = service.topScorer();
        assertTrue(result.isPresent());
        assertEquals("Fred", result.get().getPlayerName());
        assertEquals(30, result.get().getGoals());
    }

    @Test
    void testTopPenaltyScorer() {
        Optional<PlayerStats> result = service.topPenaltyScorer();
        assertTrue(result.isPresent());
        assertEquals("Fred", result.get().getPlayerName());
        assertEquals(5, result.get().getPenaltyGoals());
    }

    @Test
    void testHighestScoringMatch() {
        Optional<Match> result = service.highestScoringMatch();
        assertTrue(result.isPresent());
        Match match = result.get();
        assertEquals(4, match.totalGoals());
        assertEquals("São Paulo", match.getHomeTeam());
    }

    @Test
    void testEmptyDataset() {
        StatisticsService emptyService = new StatisticsService(
            Collections.emptyList(), 
            Collections.emptyMap()
        );
        
        assertFalse(emptyService.mostWinsInYear(2008).isPresent());
        assertFalse(emptyService.topScorer().isPresent());
        assertFalse(emptyService.highestScoringMatch().isPresent());
    }
}
