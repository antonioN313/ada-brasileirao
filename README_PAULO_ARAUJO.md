# Análise do Campeonato Brasileiro (2003-2022)

**Autor:** Paulo Araujo  
**Projeto:** Ada Brasileirão Analytics

## 📋 Descrição

Projeto desenvolvido em **Java 17** com **Gradle** para análise de dados do Campeonato Brasileiro entre 2003 e 2022. 

O sistema processa arquivos CSV contendo informações sobre partidas, gols e cartões, gerando estatísticas detalhadas usando programação funcional, Streams API e Optional.

## ✨ Funcionalidades

O sistema gera as seguintes estatísticas:

1. ⚽ **Time com mais vitórias em 2008**
2. 📍 **Estado com menos jogos no período 2003-2022**
3. 🏆 **Jogador que mais fez gols (total)**
4. 🎯 **Jogador que mais marcou de pênalti**
5. 🔴 **Jogador que mais fez gols contra**
6. 🟨 **Jogador com mais cartões amarelos**
7. 🟥 **Jogador com mais cartões vermelhos**
8. 🥅 **Partida com maior número de gols**

## 🚀 Tecnologias Utilizadas

- **Java 17** (LTS)
- **Gradle 8.x** (Build tool)
- **Stream API** (Programação funcional)
- **NIO.2** (Files e I/O moderno)
- **Optional** (Tratamento de valores opcionais)
- **JUnit 5** (Testes unitários)

## 📁 Estrutura do Projeto

```
ada-brasileirao/
├── src/
│   └── main/
│       └── java/
│           └── com/ada/brasileirao/
│               ├── app/                    # Aplicação principal
│               │   └── BrasileiraoApp.java
│               ├── controller/             # Controladores
│               │   └── BrasileiraoController.java
│               ├── model/                  # Modelos de domínio
│               │   ├── Match.java
│               │   ├── PlayerStats.java
│               │   ├── CardType.java
│               │   └── GoalType.java
│               ├── repository/             # Camada de dados
│               │   ├── CsvReader.java
│               │   ├── FileCsvReader.java
│               │   ├── CsvParser.java
│               │   ├── CsvParserImp.java
│               │   └── mapper/
│               │       ├── CsvToDomainMapper.java
│               │       └── MatchMapper.java
│               ├── service/                # Serviços de negócio
│               │   └── StatisticsService.java
│               └── exporter/               # Exportadores
│                   └── ReportExporter.java
├── data/                                   # Arquivos CSV
├── docs/                                   # Documentação e diagramas
└── build.gradle.kts                        # Configuração Gradle
```

## 🔧 Pré-requisitos

- **JDK 17** ou superior
- **Gradle** (incluído via wrapper)
- Arquivos CSV do dataset

## 📥 Instalação

### 1. Clone o repositório

```bash
git clone https://github.com/antonioN313/ada-brasileirao.git
cd ada-brasileirao
```

### 2. Prepare os dados

Coloque os arquivos CSV na pasta `data/`:
- `campeonato-brasileiro-full.csv`
- `campeonato-brasileiro-gols.csv`
- `campeonato-brasileiro-cartoes.csv`

### 3. Compile o projeto

```bash
# Linux/Mac
./gradlew clean build

# Windows
gradlew.bat clean build
```

## ▶️ Execução

### Execução básica

```bash
# Usando Gradle
./gradlew run --args="data/campeonato-brasileiro-full.csv"

# Usando JAR
java -jar build/libs/ada-brasileirao-1.0-SNAPSHOT.jar data/campeonato-brasileiro-full.csv
```

### Com arquivo de eventos (gols/cartões)

```bash
java -jar build/libs/ada-brasileirao-1.0-SNAPSHOT.jar \
  data/campeonato-brasileiro-full.csv \
  data/campeonato-brasileiro-gols.csv
```

### Exportar para JSON

```bash
java -jar build/libs/ada-brasileirao-1.0-SNAPSHOT.jar \
  data/campeonato-brasileiro-full.csv \
  data/campeonato-brasileiro-gols.csv \
  --export json --output relatorio.json
```

### Exportar para CSV

```bash
java -jar build/libs/ada-brasileirao-1.0-SNAPSHOT.jar \
  data/campeonato-brasileiro-full.csv \
  --export csv --output relatorio.csv
```

### Exportar para Markdown

```bash
java -jar build/libs/ada-brasileirao-1.0-SNAPSHOT.jar \
  data/campeonato-brasileiro-full.csv \
  --export markdown --output RELATORIO.md
```

## 📊 Exemplo de Saída

```
╔══════════════════════════════════════════════════════════════════╗
║     Análise do Campeonato Brasileiro (2003-2022)                ║
╚══════════════════════════════════════════════════════════════════╝

=== Estatísticas do Brasileirão 2003-2022 ===

📊 Time com mais vitórias em 2008: São Paulo
📍 Estado com menos jogos entre 2003 e 2022: AC
⚽ Jogador que mais fez gols: Fred
🎯 Jogador que mais fez pênaltis: Diego Souza
🔴 Jogador que mais fez gols contra: Rodrigo Caio
🟨 Jogador com mais cartões amarelos: Willian
🟥 Jogador com mais cartões vermelhos: Felipe Melo
🏆 Partida com mais gols: Flamengo 6 x 5 Vasco (2011-11-27) - Estado: RJ

Tempo de execução: 1234 ms
```

## 🧪 Testes

```bash
./gradlew test
```

## 🏗️ Arquitetura

O projeto segue princípios de **Clean Architecture** e **programação funcional**:

- **Separação de responsabilidades** (Model, Repository, Service, Controller)
- **Imutabilidade** (uso de `final` e objetos imutáveis)
- **Programação funcional** (Streams, Optional, Method References)
- **I/O moderno** (NIO.2, Files API)
- **Parser CSV robusto** (suporte a múltiplos formatos de data)

### Diagramas

Consulte a pasta `docs/diagrams/` para visualizar:
- Diagrama de componentes
- Diagrama de sequência
- Fluxograma de processamento
- Pipeline de análise

## 🤝 Contribuições

Contribuições são bem-vindas! Este projeto foi desenvolvido como parte do curso Ada Tech.

## 📄 Licença

Este projeto está sob a licença especificada no arquivo `LICENSE.txt`.

## 👤 Autor

**Paulo Araujo**
- Branch: `paulo-araujo`
- Curso: Ada Tech - Programação Java

---

*Desenvolvido com ☕ e Java 17*
