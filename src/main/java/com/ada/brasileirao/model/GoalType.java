package com.ada.brasileirao.model;

public enum GoalType {
    NORMAL,
    PENALTY,
    OWN_GOAL;

    public static GoalType fromString(String s) {
        if (s == null) return NORMAL;
        String lower = s.trim().toLowerCase();
        if (lower.contains("penal")) {
            return PENALTY;
        } else if (lower.contains("contra") || lower.contains("own")) {
            return OWN_GOAL;
        } else {
            return NORMAL;
        }
    }
}
