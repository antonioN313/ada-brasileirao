package com.ada.brasileirao.model;

public class PlayerStats {
    private final String playerName;
    private long goals = 0;
    private long penaltyGoals = 0;
    private long ownGoals = 0;
    private long yellowCards = 0;
    private long redCards = 0;

    public PlayerStats(String playerName) {
        this.playerName = playerName;
    }

    public String getPlayerName() {
        return playerName;
    }

    public long getGoals() {
        return goals;
    }

    public long getPenaltyGoals() {
        return penaltyGoals;
    }

    public long getOwnGoals() {
        return ownGoals;
    }

    public long getYellowCards() {
        return yellowCards;
    }

    public long getRedCards() {
        return redCards;
    }

    public void addGoals(long delta) {
        this.goals += delta;
    }

    public void addPenaltyGoals(long delta) {
        this.penaltyGoals += delta;
    }

    public void addOwnGoals(long delta) {
        this.ownGoals += delta;
    }

    public void addYellowCards(long delta) {
        this.yellowCards += delta;
    }

    public void addRedCards(long delta) {
        this.redCards += delta;
    }

    @Override
    public String toString() {
        return playerName
                + " [gols=" + goals
                + ", pênaltis=" + penaltyGoals
                + ", contra=" + ownGoals
                + ", amarelos=" + yellowCards
                + ", vermelhos=" + redCards + "]";
    }
}
