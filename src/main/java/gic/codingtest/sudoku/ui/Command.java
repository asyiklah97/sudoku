package gic.codingtest.sudoku.ui;

public sealed interface Command
        permits MoveCommand, ClearCommand, HintCommand, CheckCommand, QuitCommand, InvalidCommand {
}
