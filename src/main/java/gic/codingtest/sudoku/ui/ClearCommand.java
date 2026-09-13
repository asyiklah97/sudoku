package gic.codingtest.sudoku.ui;

import gic.codingtest.sudoku.domain.Position;

public record ClearCommand(Position position) implements Command {
}
