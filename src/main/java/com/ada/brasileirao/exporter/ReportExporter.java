package com.ada.brasileirao.exporter;

import com.ada.brasileirao.controller.BrasileiraoController.ResultData;
import com.ada.brasileirao.model.Match;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

/**
 * Exportador de relatórios em diferentes formatos
 */
public class ReportExporter {

    /**
     * Exporta o relatório para formato JSON
     */
    public static void exportToJson(ResultData data, Path outputPath) throws IOException {
        StringBuilder json = new StringBuilder();
        json.append("{\n");
        json.append("  \"estatisticas\": {\n");
        json.append(String.format("    \"time_mais_vitorias_2008\": \"%s\",\n", 
            escapeJson(data.getTeamMostWins2008())));
        json.append(String.format("    \"estado_menos_jogos\": \"%s\",\n", 
            escapeJson(data.getStateFewestMatches())));
        json.append(String.format("    \"jogador_mais_gols\": \"%s\",\n", 
            escapeJson(data.getTopScorer())));
        json.append(String.format("    \"jogador_mais_penaltis\": \"%s\",\n", 
            escapeJson(data.getTopPenaltyScorer())));
        json.append(String.format("    \"jogador_mais_gols_contra\": \"%s\",\n", 
            escapeJson(data.getTopOwnGoalScorer())));
        json.append(String.format("    \"jogador_mais_cartoes_amarelos\": \"%s\",\n", 
            escapeJson(data.getTopYellowCards())));
        json.append(String.format("    \"jogador_mais_cartoes_vermelhos\": \"%s\",\n", 
            escapeJson(data.getTopRedCards())));
        
        Match m = data.getHighestScoringMatch();
        if (m != null) {
            json.append("    \"partida_mais_gols\": {\n");
            json.append(String.format("      \"data\": \"%s\",\n", m.getDate()));
            json.append(String.format("      \"mandante\": \"%s\",\n", escapeJson(m.getHomeTeam())));
            json.append(String.format("      \"visitante\": \"%s\",\n", escapeJson(m.getAwayTeam())));
            json.append(String.format("      \"gols_mandante\": %d,\n", m.getHomeGoals()));
            json.append(String.format("      \"gols_visitante\": %d,\n", m.getAwayGoals()));
            json.append(String.format("      \"total_gols\": %d,\n", m.totalGoals()));
            json.append(String.format("      \"estado\": \"%s\"\n", escapeJson(m.getState())));
            json.append("    }\n");
        } else {
            json.append("    \"partida_mais_gols\": null\n");
        }
        
        json.append("  }\n");
        json.append("}\n");
        
        Files.writeString(outputPath, json.toString(), 
            StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    }

    /**
     * Exporta o relatório para formato CSV
     */
    public static void exportToCsv(ResultData data, Path outputPath) throws IOException {
        StringBuilder csv = new StringBuilder();
        csv.append("Indicador,Valor\n");
        csv.append(String.format("Time com mais vitórias em 2008,%s\n", 
            escapeCsv(data.getTeamMostWins2008())));
        csv.append(String.format("Estado com menos jogos,%s\n", 
            escapeCsv(data.getStateFewestMatches())));
        csv.append(String.format("Jogador que mais fez gols,%s\n", 
            escapeCsv(data.getTopScorer())));
        csv.append(String.format("Jogador que mais fez pênaltis,%s\n", 
            escapeCsv(data.getTopPenaltyScorer())));
        csv.append(String.format("Jogador que mais fez gols contra,%s\n", 
            escapeCsv(data.getTopOwnGoalScorer())));
        csv.append(String.format("Jogador com mais cartões amarelos,%s\n", 
            escapeCsv(data.getTopYellowCards())));
        csv.append(String.format("Jogador com mais cartões vermelhos,%s\n", 
            escapeCsv(data.getTopRedCards())));
        
        Match m = data.getHighestScoringMatch();
        if (m != null) {
            csv.append(String.format("Partida com mais gols,\"%s %d x %d %s (%s)\"\n",
                m.getHomeTeam(), m.getHomeGoals(), m.getAwayGoals(), 
                m.getAwayTeam(), m.getDate()));
        }
        
        Files.writeString(outputPath, csv.toString(), 
            StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    }

    /**
     * Exporta o relatório para Markdown
     */
    public static void exportToMarkdown(ResultData data, Path outputPath) throws IOException {
        StringBuilder md = new StringBuilder();
        md.append("# Estatísticas do Campeonato Brasileiro (2003-2022)\n\n");
        
        md.append("## Resultados\n\n");
        md.append("| Indicador | Valor |\n");
        md.append("|-----------|-------|\n");
        md.append(String.format("| Time com mais vitórias em 2008 | %s |\n", 
            data.getTeamMostWins2008()));
        md.append(String.format("| Estado com menos jogos (2003-2022) | %s |\n", 
            data.getStateFewestMatches()));
        md.append(String.format("| Jogador que mais fez gols | %s |\n", 
            data.getTopScorer()));
        md.append(String.format("| Jogador que mais fez pênaltis | %s |\n", 
            data.getTopPenaltyScorer()));
        md.append(String.format("| Jogador que mais fez gols contra | %s |\n", 
            data.getTopOwnGoalScorer()));
        md.append(String.format("| Jogador com mais cartões amarelos | %s |\n", 
            data.getTopYellowCards()));
        md.append(String.format("| Jogador com mais cartões vermelhos | %s |\n", 
            data.getTopRedCards()));
        
        Match m = data.getHighestScoringMatch();
        if (m != null) {
            md.append("\n## Partida com Mais Gols\n\n");
            md.append(String.format("- **Data:** %s\n", m.getDate()));
            md.append(String.format("- **Mandante:** %s (%d gols)\n", 
                m.getHomeTeam(), m.getHomeGoals()));
            md.append(String.format("- **Visitante:** %s (%d gols)\n", 
                m.getAwayTeam(), m.getAwayGoals()));
            md.append(String.format("- **Total de gols:** %d\n", m.totalGoals()));
            md.append(String.format("- **Estado:** %s\n", m.getState()));
        }
        
        Files.writeString(outputPath, md.toString(), 
            StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    }

    private static String escapeJson(String s) {
        if (s == null) return "null";
        return s.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }

    private static String escapeCsv(String s) {
        if (s == null) return "";
        if (s.contains(",") || s.contains("\"") || s.contains("\n")) {
            return "\"" + s.replace("\"", "\"\"") + "\"";
        }
        return s;
    }
}
