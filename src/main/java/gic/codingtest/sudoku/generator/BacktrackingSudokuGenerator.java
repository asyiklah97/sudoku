package gic.codingtest.sudoku.generator;

import gic.codingtest.sudoku.domain.Position;
import gic.codingtest.sudoku.domain.SudokuBoard;
import gic.codingtest.sudoku.domain.SudokuPuzzle;
import gic.codingtest.sudoku.service.SudokuSolver;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public final class BacktrackingSudokuGenerator implements SudokuGenerator {
    private static final int GIVEN_CELLS = 30;
    private final SudokuSolver solver;
    private final Random random;

    public BacktrackingSudokuGenerator(SudokuSolver solver) {
        this(solver, new Random());
    }

    public BacktrackingSudokuGenerator(SudokuSolver solver, Random random) {
        this.solver = solver;
        this.random = random;
    }

    @Override
    public SudokuPuzzle generate() {
        int[][] solution = createSolvedGrid();
        int[][] puzzle = copy(solution);
        removeCellsWhileUnique(puzzle);

        return new SudokuPuzzle(puzzle, solution);
    }

    private int[][] createSolvedGrid() {
        SudokuBoard board = SudokuBoard.empty();
        fill(board);
        return board.toArray();
    }

    private boolean fill(SudokuBoard board) {
        List<Position> empty = emptyPositions(board);
        if (empty.isEmpty()) {
            return true;
        }

        Position position = empty.get(0);
        List<Integer> numbers = shuffledNumbers();

        for (int number : numbers) {
            if (canPlace(board, position, number)) {
                board.set(position, number);
                if (fill(board)) {
                    return true;
                }
                board.clear(position);
            }
        }

        return false;
    }

    private void removeCellsWhileUnique(int[][] puzzle) {
        List<Position> positions = allPositions();
        Collections.shuffle(positions, random);

        for (Position position : positions) {
            if (countValues(puzzle) <= GIVEN_CELLS) {
                return;
            }

            int original = puzzle[position.row()][position.column()];
            puzzle[position.row()][position.column()] = 0;

            SudokuBoard candidate = SudokuBoard.from(puzzle);
            if (solver.countSolutions(candidate, 2) != 1) {
                puzzle[position.row()][position.column()] = original;
            }
        }

        if (countValues(puzzle) != GIVEN_CELLS) {
            throw new IllegalStateException(
                    "Could not generate a puzzle with exactly 30 clues."
            );
        }
    }

    private boolean canPlace(
            SudokuBoard board,
            Position position,
            int number) {

        int[][] values = board.toArray();

        for (int i = 0; i < 9; i++) {
            if (values[position.row()][i] == number
                    || values[i][position.column()] == number) {
                return false;
            }
        }

        int startRow = (position.row() / 3) * 3;
        int startCol = (position.column() / 3) * 3;

        for (int row = startRow; row < startRow + 3; row++) {
            for (int col = startCol; col < startCol + 3; col++) {
                if (values[row][col] == number) {
                    return false;
                }
            }
        }

        return true;
    }

    private List<Position> emptyPositions(SudokuBoard board) {
        List<Position> result = new ArrayList<>();
        int[][] values = board.toArray();

        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                if (values[row][col] == 0) {
                    result.add(new Position(row, col));
                }
            }
        }

        return result;
    }

    private List<Position> allPositions() {
        List<Position> result = new ArrayList<>();
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                result.add(new Position(row, col));
            }
        }
        return result;
    }

    private List<Integer> shuffledNumbers() {
        List<Integer> result = new ArrayList<>();
        for (int i = 1; i <= 9; i++) {
            result.add(i);
        }
        Collections.shuffle(result, random);
        return result;
    }

    private int countValues(int[][] board) {
        int count = 0;
        for (int[] row : board) {
            for (int value : row) {
                if (value != 0) {
                    count++;
                }
            }
        }
        return count;
    }

    private int[][] copy(int[][] source) {
        int[][] result = new int[source.length][];
        for (int i = 0; i < source.length; i++) {
            result[i] = source[i].clone();
        }
        return result;
    }
}
