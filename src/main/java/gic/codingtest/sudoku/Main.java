package gic.codingtest.sudoku;

import gic.codingtest.sudoku.generator.BacktrackingSudokuGenerator;
import gic.codingtest.sudoku.generator.SudokuGenerator;
import gic.codingtest.sudoku.service.SudokuGame;
import gic.codingtest.sudoku.service.SudokuSolver;
import gic.codingtest.sudoku.service.SudokuValidator;
import gic.codingtest.sudoku.ui.CommandParser;
import gic.codingtest.sudoku.ui.ConsoleSudokuUI;

public final class Main {
    private Main() {
    }

    public static void main(String[] args) {
        SudokuValidator validator = new SudokuValidator();
        SudokuSolver solver = new SudokuSolver(validator);
        SudokuGenerator generator = new BacktrackingSudokuGenerator(solver);
        SudokuGame game = new SudokuGame(generator, validator, solver);
        ConsoleSudokuUI ui = new ConsoleSudokuUI(game, new CommandParser());
        ui.run();
    }
}
