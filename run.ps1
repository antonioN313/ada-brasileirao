# Script de execução para Windows PowerShell

Write-Host "╔══════════════════════════════════════════════════════════════════╗" -ForegroundColor Cyan
Write-Host "║     Análise do Campeonato Brasileiro (2003-2022)                ║" -ForegroundColor Cyan
Write-Host "╚══════════════════════════════════════════════════════════════════╝" -ForegroundColor Cyan
Write-Host ""

# Compilar projeto
Write-Host "📦 Compilando projeto..." -ForegroundColor Yellow
./gradlew.bat clean build -q

if ($LASTEXITCODE -ne 0) {
    Write-Host "❌ Erro na compilação!" -ForegroundColor Red
    exit 1
}

Write-Host "✅ Compilação concluída!" -ForegroundColor Green
Write-Host ""

# Executar análise
Write-Host "🔍 Executando análise..." -ForegroundColor Yellow
java -jar build/libs/ada-brasileirao-1.0-SNAPSHOT.jar `
    data/campeonato-brasileiro-full.csv `
    data/campeonato-brasileiro-gols.csv `
    --export json --output relatorio.json

if ($LASTEXITCODE -eq 0) {
    Write-Host ""
    Write-Host "✅ Análise concluída com sucesso!" -ForegroundColor Green
    Write-Host "📄 Relatório JSON gerado: relatorio.json" -ForegroundColor Cyan
}
