package com.ada.brasileirao.app;

import com.ada.brasileirao.controller.BrasileiraoController;
import com.ada.brasileirao.controller.BrasileiraoController.ResultData;
import com.ada.brasileirao.model.Match;

import java.nio.file.Path;

public class BrasileiraoApp {
    public static void main(String[] args) {
        if (args.length < 1) {
            System.err.println("Uso: java -jar <jar> <arquivo_partidas.csv> [<arquivo_eventos.csv>]");
            System.exit(1);
        }

        Path pathMatches = Path.of(args[0]);
        Path pathEvents = (args.length >= 2 ? Path.of(args[1]) : null);

        BrasileiraoController controller = new BrasileiraoController();
        ResultData res = controller.computeStatistics(pathMatches, pathEvents);

        System.out.println("=== Estatísticas do Brasileirão 2003‑2022 ===");
        System.out.println("Time com mais vitórias em 2008: " + res.getTeamMostWins2008());
        System.out.println("Estado com menos jogos entre 2003 e 2022: " + res.getStateFewestMatches());
        System.out.println("Jogador que mais fez gols: " + res.getTopScorer());
        System.out.println("Jogador que mais fez pênaltis: " + res.getTopPenaltyScorer());
        System.out.println("Jogador que mais fez gols contra: " + res.getTopOwnGoalScorer());
        System.out.println("Jogador com mais cartões amarelos: " + res.getTopYellowCards());
        System.out.println("Jogador com mais cartões vermelhos: " + res.getTopRedCards());
        Match m = res.getHighestScoringMatch();
        System.out.println("Partida com mais gols: " + (m != null ? m.toString() : "nenhuma"));
    }
}
