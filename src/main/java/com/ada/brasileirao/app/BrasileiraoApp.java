package com.ada.brasileirao.app;

import com.ada.brasileirao.controller.BrasileiraoController;
import com.ada.brasileirao.controller.BrasileiraoController.ResultData;
import com.ada.brasileirao.exporter.ReportExporter;
import com.ada.brasileirao.model.Match;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

public class BrasileiraoApp {
    public static void main(String[] args) {
        // Configura UTF-8 para o console
        try {
            System.setOut(new PrintStream(System.out, true, StandardCharsets.UTF_8));
        } catch (Exception e) {
            // Se falhar, continua com a codificação padrão
        }
        
        if (args.length < 1) {
            printUsage();
            System.exit(1);
        }

        Path pathMatches = Path.of(args[0]);
        Path pathEvents = (args.length >= 2 && !args[1].startsWith("--")) ? Path.of(args[1]) : null;

        // Verifica opções de exportação
        String exportFormat = null;
        Path exportPath = null;
        
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("--export") && i + 1 < args.length) {
                exportFormat = args[i + 1];
                i++;
            } else if (args[i].equals("--output") && i + 1 < args.length) {
                exportPath = Path.of(args[i + 1]);
                i++;
            }
        }

        System.out.println("╔══════════════════════════════════════════════════════════════════╗");
        System.out.println("║     Análise do Campeonato Brasileiro (2003-2022)                ║");
        System.out.println("╚══════════════════════════════════════════════════════════════════╝");
        System.out.println();

        long startTime = System.currentTimeMillis();
        
        BrasileiraoController controller = new BrasileiraoController();
        ResultData res = controller.computeStatistics(pathMatches, pathEvents);

        long endTime = System.currentTimeMillis();

        // Exibe resultados no console
        printResults(res);
        
        System.out.println();
        System.out.println("Tempo de execução: " + (endTime - startTime) + " ms");
        
        // Exporta se solicitado
        if (exportFormat != null && exportPath != null) {
            try {
                exportResults(res, exportFormat, exportPath);
                System.out.println("\n✓ Relatório exportado para: " + exportPath);
            } catch (IOException e) {
                System.err.println("✗ Erro ao exportar: " + e.getMessage());
            }
        }
    }
    
    private static void printResults(ResultData res) {
        System.out.println("=== Estatísticas do Brasileirão 2003-2022 ===\n");
        System.out.println("📊 Time com mais vitórias em 2008: " + 
            (res.getTeamMostWins2008() != null ? res.getTeamMostWins2008() : "N/A"));
        System.out.println("📍 Estado com menos jogos entre 2003 e 2022: " + 
            (res.getStateFewestMatches() != null ? res.getStateFewestMatches() : "N/A"));
        System.out.println("⚽ Jogador que mais fez gols: " + 
            (res.getTopScorer() != null ? res.getTopScorer() : "N/A"));
        System.out.println("🎯 Jogador que mais fez pênaltis: " + 
            (res.getTopPenaltyScorer() != null ? res.getTopPenaltyScorer() : "N/A"));
        System.out.println("🔴 Jogador que mais fez gols contra: " + 
            (res.getTopOwnGoalScorer() != null ? res.getTopOwnGoalScorer() : "N/A"));
        System.out.println("🟨 Jogador com mais cartões amarelos: " + 
            (res.getTopYellowCards() != null ? res.getTopYellowCards() : "N/A"));
        System.out.println("🟥 Jogador com mais cartões vermelhos: " + 
            (res.getTopRedCards() != null ? res.getTopRedCards() : "N/A"));
        
        Match m = res.getHighestScoringMatch();
        System.out.println("🏆 Partida com mais gols: " + 
            (m != null ? m.toString() : "nenhuma"));
    }
    
    private static void exportResults(ResultData res, String format, Path outputPath) 
            throws IOException {
        switch (format.toLowerCase()) {
            case "json" -> ReportExporter.exportToJson(res, outputPath);
            case "csv" -> ReportExporter.exportToCsv(res, outputPath);
            case "markdown", "md" -> ReportExporter.exportToMarkdown(res, outputPath);
            default -> throw new IllegalArgumentException("Formato não suportado: " + format);
        }
    }
    
    private static void printUsage() {
        System.out.println("Uso: java -jar brasileirao.jar <arquivo_partidas.csv> [<arquivo_eventos.csv>] [opções]");
        System.out.println();
        System.out.println("Opções:");
        System.out.println("  --export <formato>   Formato de exportação: json, csv, markdown");
        System.out.println("  --output <arquivo>   Arquivo de saída para exportação");
        System.out.println();
        System.out.println("Exemplos:");
        System.out.println("  java -jar brasileirao.jar data/campeonato-brasileiro-full.csv");
        System.out.println("  java -jar brasileirao.jar data/campeonato-brasileiro-full.csv data/campeonato-brasileiro-gols.csv");
        System.out.println("  java -jar brasileirao.jar data/partidas.csv --export json --output relatorio.json");
    }
}
