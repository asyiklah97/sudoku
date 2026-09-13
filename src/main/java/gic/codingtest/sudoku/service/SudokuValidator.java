package gic.codingtest.sudoku.service;

import gic.codingtest.sudoku.domain.SudokuBoard;

import java.util.ArrayList;
import java.util.List;

public final class SudokuValidator {

    public List<SudokuViolation> validate(SudokuBoard board) {
        List<SudokuViolation> violations = new ArrayList<>();

        validateRows(board, violations);
        validateColumns(board, violations);
        validateSubgrids(board, violations);

        return violations;
    }

    public boolean isValid(SudokuBoard board) {
        return validate(board).isEmpty();
    }

    private void validateRows(
            SudokuBoard board,
            List<SudokuViolation> violations) {

        for (int row = 0; row < SudokuBoard.SIZE; row++) {
            for (int number = 1; number <= 9; number++) {
                if (countInRow(board, row, number) > 1) {
                    violations.add(
                            new SudokuViolation(number, ViolationType.ROW, row)
                    );
                }
            }
        }
    }

    private void validateColumns(
            SudokuBoard board,
            List<SudokuViolation> violations) {

        for (int col = 0; col < SudokuBoard.SIZE; col++) {
            for (int number = 1; number <= 9; number++) {
                if (countInColumn(board, col, number) > 1) {
                    violations.add(
                            new SudokuViolation(
                                    number, ViolationType.COLUMN, col)
                    );
                }
            }
        }
    }

    private void validateSubgrids(
            SudokuBoard board,
            List<SudokuViolation> violations) {

        for (int row = 0; row < SudokuBoard.SIZE; row += 3) {
            for (int col = 0; col < SudokuBoard.SIZE; col += 3) {
                for (int number = 1; number <= 9; number++) {
                    if (countInSubgrid(board, row, col, number) > 1) {
                        violations.add(
                                new SudokuViolation(
                                        number,
                                        ViolationType.SUBGRID,
                                        row * SudokuBoard.SIZE + col)
                        );
                    }
                }
            }
        }
    }

    private int countInRow(SudokuBoard board, int row, int number) {
        int count = 0;
        for (int col = 0; col < SudokuBoard.SIZE; col++) {
            if (board.toArray()[row][col] == number) {
                count++;
            }
        }
        return count;
    }

    private int countInColumn(SudokuBoard board, int col, int number) {
        int count = 0;
        int[][] values = board.toArray();
        for (int row = 0; row < SudokuBoard.SIZE; row++) {
            if (values[row][col] == number) {
                count++;
            }
        }
        return count;
    }

    private int countInSubgrid(
            SudokuBoard board,
            int startRow,
            int startCol,
            int number) {

        int count = 0;
        int[][] values = board.toArray();

        for (int row = startRow; row < startRow + 3; row++) {
            for (int col = startCol; col < startCol + 3; col++) {
                if (values[row][col] == number) {
                    count++;
                }
            }
        }

        return count;
    }
}
