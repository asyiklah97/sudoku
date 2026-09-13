package gic.codingtest.sudoku.generator;

import gic.codingtest.sudoku.domain.SudokuBoard;
import gic.codingtest.sudoku.domain.SudokuPuzzle;
import gic.codingtest.sudoku.service.SudokuSolver;
import gic.codingtest.sudoku.service.SudokuValidator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BacktrackingSudokuGeneratorTest {

    @Test
    void generatesExactly30CluesAndUniqueSolution() {
        SudokuValidator validator = new SudokuValidator();
        SudokuSolver solver = new SudokuSolver(validator);
        SudokuGenerator generator =
                new BacktrackingSudokuGenerator(solver);

        SudokuPuzzle puzzle = generator.generate();

        assertEquals(30, puzzle.numberOfGivenCells());

        SudokuBoard board = SudokuBoard.from(puzzle.givens());

        assertTrue(validator.isValid(board));
        assertEquals(1, solver.countSolutions(board, 2));

        int[][] solution = puzzle.solution();
        assertTrue(
                validator.isValid(SudokuBoard.from(solution))
        );
    }

    @Test
    void givensMatchSolution() {
        SudokuValidator validator = new SudokuValidator();
        SudokuSolver solver = new SudokuSolver(validator);
        SudokuPuzzle puzzle =
                new BacktrackingSudokuGenerator(solver).generate();

        int[][] givens = puzzle.givens();
        int[][] solution = puzzle.solution();

        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                if (givens[row][col] != 0) {
                    assertEquals(solution[row][col], givens[row][col]);
                }
            }
        }
    }
}
