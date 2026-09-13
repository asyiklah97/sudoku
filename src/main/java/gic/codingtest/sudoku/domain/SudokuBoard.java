package gic.codingtest.sudoku.domain;

import java.util.Arrays;

public final class SudokuBoard {
    public static final int SIZE = 9;
    public static final int BOX_SIZE = 3;

    private final int[][] values;
    private final boolean[][] fixed;

    private SudokuBoard(int[][] values, boolean[][] fixed) {
        this.values = copy(values);
        this.fixed = copy(fixed);
    }

    public static SudokuBoard from(int[][] puzzle) {
        validateMatrix(puzzle);
        boolean[][] fixed = new boolean[SIZE][SIZE];

        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                int value = puzzle[row][col];
                if (value < 0 || value > 9) {
                    throw new IllegalArgumentException("Values must be 0-9.");
                }
                fixed[row][col] = value != 0;
            }
        }

        return new SudokuBoard(puzzle, fixed);
    }

    public static SudokuBoard empty() {
        return from(new int[SIZE][SIZE]);
    }

    public int get(Position position) {
        return values[position.row()][position.column()];
    }

    public boolean isFixed(Position position) {
        return fixed[position.row()][position.column()];
    }

    public void set(Position position, int value) {
        if (isFixed(position)) {
            throw new FixedCellException(position + " is pre-filled.");
        }
        validateValue(value);
        values[position.row()][position.column()] = value;
    }

    public void clear(Position position) {
        if (isFixed(position)) {
            throw new FixedCellException(position + " is pre-filled.");
        }
        values[position.row()][position.column()] = 0;
    }

    public boolean isComplete() {
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                if (values[row][col] == 0) {
                    return false;
                }
            }
        }
        return true;
    }

    public int countNonEmpty() {
        int count = 0;
        for (int[] row : values) {
            for (int value : row) {
                if (value != 0) {
                    count++;
                }
            }
        }
        return count;
    }

    public int[][] toArray() {
        return copy(values);
    }

    public SudokuBoard copy() {
        return new SudokuBoard(values, fixed);
    }

    public void replaceValues(int[][] newValues) {
        validateMatrix(newValues);
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                Position position = new Position(row, col);
                if (fixed[row][col] && values[row][col] != newValues[row][col]) {
                    throw new FixedCellException(position + " is pre-filled.");
                }
                validateValue(newValues[row][col]);
            }
        }
        for (int row = 0; row < SIZE; row++) {
            System.arraycopy(newValues[row], 0, values[row], 0, SIZE);
        }
    }

    private static void validateValue(int value) {
        if (value < 1 || value > 9) {
            throw new IllegalArgumentException("Number must be between 1 and 9.");
        }
    }

    private static void validateMatrix(int[][] matrix) {
        if (matrix == null || matrix.length != SIZE) {
            throw new IllegalArgumentException("Board must be 9x9.");
        }
        for (int[] row : matrix) {
            if (row == null || row.length != SIZE) {
                throw new IllegalArgumentException("Board must be 9x9.");
            }
        }
    }

    private static int[][] copy(int[][] source) {
        int[][] result = new int[source.length][];
        for (int i = 0; i < source.length; i++) {
            result[i] = Arrays.copyOf(source[i], source[i].length);
        }
        return result;
    }

    private static boolean[][] copy(boolean[][] source) {
        boolean[][] result = new boolean[source.length][];
        for (int i = 0; i < source.length; i++) {
            result[i] = Arrays.copyOf(source[i], source[i].length);
        }
        return result;
    }
}
