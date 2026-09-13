package gic.codingtest.sudoku.service;

import gic.codingtest.sudoku.domain.SudokuBoard;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SudokuValidatorTest {
    private final SudokuValidator validator = new SudokuValidator();

    @Test
    void detectsRowViolation() {
        SudokuBoard board = board(
                new int[][]{
                        {5, 3, 3, 0, 7, 0, 0, 0, 0},
                        {6, 0, 0, 1, 9, 5, 0, 0, 0},
                        {0, 9, 8, 0, 0, 0, 0, 6, 0},
                        {8, 0, 0, 0, 6, 0, 0, 0, 3},
                        {4, 0, 0, 8, 0, 3, 0, 0, 1},
                        {7, 0, 0, 0, 2, 0, 0, 0, 6},
                        {0, 6, 0, 0, 0, 0, 2, 8, 0},
                        {0, 0, 0, 4, 1, 9, 0, 0, 5},
                        {0, 0, 0, 0, 8, 0, 0, 7, 9}
                }
        );

        List<SudokuViolation> violations = validator.validate(board);

        assertTrue(violations.contains(
                new SudokuViolation(3, ViolationType.ROW, 0)
        ));
    }

    @Test
    void detectsColumnViolation() {
        SudokuBoard board = board(
                new int[][]{
                        {5, 3, 0, 0, 7, 0, 0, 0, 0},
                        {6, 0, 0, 1, 9, 5, 0, 0, 0},
                        {5, 9, 8, 0, 0, 0, 0, 6, 0},
                        {8, 0, 0, 0, 6, 0, 0, 0, 3},
                        {4, 0, 0, 8, 0, 3, 0, 0, 1},
                        {7, 0, 0, 0, 2, 0, 0, 0, 6},
                        {0, 6, 0, 0, 0, 0, 2, 8, 0},
                        {0, 0, 0, 4, 1, 9, 0, 0, 5},
                        {0, 0, 0, 0, 8, 0, 0, 7, 9}
                }
        );

        assertTrue(validator.validate(board).contains(
                new SudokuViolation(5, ViolationType.COLUMN, 0)
        ));
    }

    @Test
    void detectsSubgridViolation() {
        SudokuBoard board = board(
                new int[][]{
                        {5, 3, 0, 0, 7, 0, 0, 0, 0},
                        {6, 0, 8, 1, 9, 5, 0, 0, 0},
                        {0, 9, 8, 0, 0, 0, 0, 6, 0},
                        {8, 0, 0, 0, 6, 0, 0, 0, 3},
                        {4, 0, 0, 8, 0, 3, 0, 0, 1},
                        {7, 0, 0, 0, 2, 0, 0, 0, 6},
                        {0, 6, 0, 0, 0, 0, 2, 8, 0},
                        {0, 0, 0, 4, 1, 9, 0, 0, 5},
                        {0, 0, 0, 0, 8, 0, 0, 7, 9}
                }
        );

        assertTrue(validator.validate(board).stream().anyMatch(
                v -> v.number() == 8 && v.type() == ViolationType.SUBGRID
        ));
    }

    @Test
    void validIncompleteBoardHasNoViolations() {
        SudokuBoard board = SudokuBoard.from(new int[][]{
                {5, 3, 0, 0, 7, 0, 0, 0, 0},
                {6, 0, 0, 1, 9, 5, 0, 0, 0},
                {0, 9, 8, 0, 0, 0, 0, 6, 0},
                {8, 0, 0, 0, 6, 0, 0, 0, 3},
                {4, 0, 0, 8, 0, 3, 0, 0, 1},
                {7, 0, 0, 0, 2, 0, 0, 0, 6},
                {0, 6, 0, 0, 0, 0, 2, 8, 0},
                {0, 0, 0, 4, 1, 9, 0, 0, 5},
                {0, 0, 0, 0, 8, 0, 0, 7, 9}
        });

        assertTrue(validator.isValid(board));
    }

    private SudokuBoard board(int[][] values) {
        return SudokuBoard.from(values);
    }
}
