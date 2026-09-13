package gic.codingtest.sudoku.service;

import gic.codingtest.sudoku.domain.Position;
import gic.codingtest.sudoku.domain.SudokuBoard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public final class SudokuSolver {
    private final SudokuValidator validator;

    public SudokuSolver(SudokuValidator validator) {
        this.validator = validator;
    }

    public Optional<int[][]> solve(SudokuBoard board) {
        if (!validator.isValid(board)) {
            return Optional.empty();
        }

        SudokuBoard working = board.copy();
        if (solveRecursive(working)) {
            return Optional.of(working.toArray());
        }
        return Optional.empty();
    }

    public int countSolutions(SudokuBoard board, int limit) {
        if (limit < 1) {
            throw new IllegalArgumentException("Limit must be positive.");
        }
        if (!validator.isValid(board)) {
            return 0;
        }
        return countSolutionsRecursive(board.copy(), limit);
    }

    private boolean solveRecursive(SudokuBoard board) {
        Optional<int[]> empty = findEmptyCell(board);

        if (empty.isEmpty()) {
            return true;
        }

        int row = empty.get()[0];
        int col = empty.get()[1];
        List<Integer> candidates = candidates(board, row, col);

        for (int value : candidates) {
            board.set(new Position(row, col), value);

            if (solveRecursive(board)) {
                return true;
            }

            board.clear(new Position(row, col));
        }

        return false;
    }

    private int countSolutionsRecursive(SudokuBoard board, int limit) {
        Optional<int[]> empty = findEmptyCell(board);

        if (empty.isEmpty()) {
            return 1;
        }

        int row = empty.get()[0];
        int col = empty.get()[1];
        int count = 0;

        for (int value : candidates(board, row, col)) {
            var position = new Position(row, col);
            board.set(position, value);
            count += countSolutionsRecursive(board, limit - count);
            board.clear(position);

            if (count >= limit) {
                return count;
            }
        }

        return count;
    }

    private Optional<int[]> findEmptyCell(SudokuBoard board) {
        int[][] values = board.toArray();
        int bestCount = Integer.MAX_VALUE;
        int[] best = null;

        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                if (values[row][col] == 0) {
                    int candidateCount = candidates(board, row, col).size();
                    if (candidateCount < bestCount) {
                        bestCount = candidateCount;
                        best = new int[]{row, col};
                    }
                }
            }
        }

        return Optional.ofNullable(best);
    }

    private List<Integer> candidates(
            SudokuBoard board,
            int row,
            int col) {

        boolean[] used = new boolean[10];
        int[][] values = board.toArray();

        for (int i = 0; i < 9; i++) {
            used[values[row][i]] = true;
            used[values[i][col]] = true;
        }

        int startRow = (row / 3) * 3;
        int startCol = (col / 3) * 3;

        for (int r = startRow; r < startRow + 3; r++) {
            for (int c = startCol; c < startCol + 3; c++) {
                used[values[r][c]] = true;
            }
        }

        List<Integer> result = new ArrayList<>();
        for (int value = 1; value <= 9; value++) {
            if (!used[value]) {
                result.add(value);
            }
        }

        Collections.shuffle(result);
        return result;
    }
}
