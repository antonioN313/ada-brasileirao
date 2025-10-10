package com.ada.brasileirao.model;

import java.time.LocalDate;
import java.util.Objects;

public class Match {
    private final LocalDate date;
    private final String homeTeam;
    private final String awayTeam;
    private final int homeGoals;
    private final int awayGoals;
    private final String state;

    public Match(LocalDate date, String homeTeam, String awayTeam,
                 int homeGoals, int awayGoals, String state) {
        this.date = date;
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.homeGoals = homeGoals;
        this.awayGoals = awayGoals;
        this.state = state;
    }

    public LocalDate getDate() { return date; }
    public String getHomeTeam() { return homeTeam; }
    public String getAwayTeam() { return awayTeam; }
    public int getHomeGoals() { return homeGoals; }
    public int getAwayGoals() { return awayGoals; }
    public String getState() { return state; }

    public int totalGoals() {
        return homeGoals + awayGoals;
    }

    @Override
    public String toString() {
        return homeTeam + " " + homeGoals + " x " + awayGoals + " " + awayTeam
                + " (" + date + ") - Estado: " + state;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Match)) return false;
        Match other = (Match) o;
        return Objects.equals(date, other.date)
                && Objects.equals(homeTeam, other.homeTeam)
                && Objects.equals(awayTeam, other.awayTeam)
                && homeGoals == other.homeGoals
                && awayGoals == other.awayGoals
                && Objects.equals(state, other.state);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date, homeTeam, awayTeam, homeGoals, awayGoals, state);
    }
}
