# Sudoku CLI

A production-quality command-line Sudoku game written in Java 21.

## Requirements

- Java 21+
- Maven 3.9+

Works on Windows, macOS and Linux.

## Build

```bash
mvn clean test
mvn package
```

## Run

```bash
java -jar target/sudoku-1.0.0.jar
```

## Commands

```text
A3 4       Put 4 in A3
C5 clear   Clear C5
hint       Reveal one correct number
check      Check rows, columns and 3x3 subgrids
quit       Exit the game
```

A player may not modify a pre-filled cell. Values must be 1-9.

The generated puzzle contains exactly 30 pre-filled cells. The generator creates a complete valid board, then removes values while ensuring the puzzle has a unique solution.

## Design

- `SudokuBoard` owns mutable board state and fixed-cell rules.
- `SudokuValidator` validates Sudoku constraints.
- `SudokuSolver` solves boards and is also used to verify uniqueness.
- `SudokuGenerator` is an abstraction for puzzle generation.
- `BacktrackingSudokuGenerator` creates puzzles without external Sudoku libraries.
- `CommandParser` parses CLI commands independently of I/O.
- `SudokuGame` contains application/game orchestration.
- `ConsoleSudokuUI` owns console input/output.
- JUnit tests cover domain behavior, parsing, validation, generation, game behavior and an end-to-end CLI flow.

The application does not depend on any external library for Sudoku solving or generation. JUnit is test-only.
