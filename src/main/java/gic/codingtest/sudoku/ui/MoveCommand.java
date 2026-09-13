package gic.codingtest.sudoku.ui;

import gic.codingtest.sudoku.domain.Position;

public record MoveCommand(Position position, int value) implements Command {
}
