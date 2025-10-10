package com.ada.brasileirao.model;

public enum CardType {
    NONE,
    YELLOW,
    RED;

    public static CardType fromString(String s) {
        if (s == null || s.isBlank()) {
            return NONE;
        }
        String lower = s.trim().toLowerCase();
        if (lower.contains("amarelo") || lower.contains("yellow")) {
            return YELLOW;
        } else if (lower.contains("vermelho") || lower.contains("red")) {
            return RED;
        } else {
            return NONE;
        }
    }
}
