#!/bin/bash
# Script de execução para Linux/Mac

echo "╔══════════════════════════════════════════════════════════════════╗"
echo "║     Análise do Campeonato Brasileiro (2003-2022)                ║"
echo "╚══════════════════════════════════════════════════════════════════╝"
echo ""

# Compilar projeto
echo "📦 Compilando projeto..."
./gradlew clean build -q

if [ $? -ne 0 ]; then
    echo "❌ Erro na compilação!"
    exit 1
fi

echo "✅ Compilação concluída!"
echo ""

# Executar análise
echo "🔍 Executando análise..."
java -jar build/libs/ada-brasileirao-1.0-SNAPSHOT.jar \
    data/campeonato-brasileiro-full.csv \
    data/campeonato-brasileiro-gols.csv \
    --export json --output relatorio.json

if [ $? -eq 0 ]; then
    echo ""
    echo "✅ Análise concluída com sucesso!"
    echo "📄 Relatório JSON gerado: relatorio.json"
fi
