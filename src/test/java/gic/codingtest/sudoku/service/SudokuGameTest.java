package gic.codingtest.sudoku.service;

import gic.codingtest.sudoku.domain.Position;
import gic.codingtest.sudoku.domain.SudokuPuzzle;
import gic.codingtest.sudoku.generator.SudokuGenerator;
import gic.codingtest.sudoku.ui.CheckCommand;
import gic.codingtest.sudoku.ui.ClearCommand;
import gic.codingtest.sudoku.ui.MoveCommand;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SudokuGameTest {

    private static final int[][] PUZZLE = {
            {5, 3, 0, 0, 7, 0, 0, 0, 0},
            {6, 0, 0, 1, 9, 5, 0, 0, 0},
            {0, 9, 8, 0, 0, 0, 0, 6, 0},
            {8, 0, 0, 0, 6, 0, 0, 0, 3},
            {4, 0, 0, 8, 0, 3, 0, 0, 1},
            {7, 0, 0, 0, 2, 0, 0, 0, 6},
            {0, 6, 0, 0, 0, 0, 2, 8, 0},
            {0, 0, 0, 4, 1, 9, 0, 0, 5},
            {0, 0, 0, 0, 8, 0, 0, 7, 9}
    };

    private static final int[][] SOLUTION = {
            {5, 3, 4, 6, 7, 8, 9, 1, 2},
            {6, 7, 2, 1, 9, 5, 3, 4, 8},
            {1, 9, 8, 3, 4, 2, 5, 6, 7},
            {8, 5, 9, 7, 6, 1, 4, 2, 3},
            {4, 2, 6, 8, 5, 3, 7, 9, 1},
            {7, 1, 3, 9, 2, 4, 8, 5, 6},
            {9, 6, 1, 5, 3, 7, 2, 8, 4},
            {2, 8, 7, 4, 1, 9, 6, 3, 5},
            {3, 4, 5, 2, 8, 6, 1, 7, 9}
    };

    @Test
    void acceptsMoveToEmptyCell() {
        SudokuGame game = game();

        GameResult result = game.execute(
                new MoveCommand(
                        Position.parse("A3"), 4)
        );

        assertEquals("Move accepted.", result.message());
        assertEquals(
                4,
                game.board().get(
                        Position.parse("A3"))
        );
    }

    @Test
    void rejectsChangeToFixedCell() {
        SudokuGame game = game();

        GameResult result = game.execute(
                new MoveCommand(
                        Position.parse("A1"), 6)
        );

        assertEquals(
                "Invalid move. A1 is pre-filled.",
                result.message()
        );
    }

    @Test
    void checkReportsRowViolation() {
        SudokuGame game = game();

        game.execute(new MoveCommand(
                Position.parse("A3"), 3));

        GameResult result = game.execute(new CheckCommand());

        assertTrue(result.message().contains(
                "Number 3 already exists in Row A."
        ));
    }

    @Test
    void clearRemovesUserValue() {
        SudokuGame game = game();

        game.execute(new MoveCommand(
                Position.parse("A3"), 4));

        game.execute(new ClearCommand(
                Position.parse("A3")));

        assertEquals(
                0,
                game.board().get(
                        Position.parse("A3"))
        );
    }

    @Test
    void solvingPuzzleCompletesGame() {
        SudokuGame game = game();

        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                if (PUZZLE[row][col] == 0) {
                    game.execute(new MoveCommand(
                            new Position(row, col),
                            SOLUTION[row][col]));
                }
            }
        }

        assertTrue(game.board().isComplete());
        assertTrue(game.execute(new CheckCommand()).message()
                .contains("No rule violations"));
    }

    private SudokuGame game() {
        SudokuValidator validator = new SudokuValidator();
        SudokuSolver solver = new SudokuSolver(validator);

        SudokuGenerator generator = () ->
                new SudokuPuzzle(PUZZLE, SOLUTION);

        return new SudokuGame(generator, validator, solver);
    }
}
