package gic.codingtest.sudoku.domain;

public record Position(int row, int column) {
    public Position {
        if (row < 0 || row >= SudokuBoard.SIZE) {
            throw new IllegalArgumentException("Row must be between A and I.");
        }
        if (column < 0 || column >= SudokuBoard.SIZE) {
            throw new IllegalArgumentException("Column must be between 1 and 9.");
        }
    }

    public static Position parse(String value) {
        if (value == null || !value.matches("[A-Ia-i][1-9]")) {
            throw new IllegalArgumentException(
                    "Cell must be in the form A1-I9."
            );
        }

        return new Position(
                Character.toUpperCase(value.charAt(0)) - 'A',
                value.charAt(1) - '1'
        );
    }

    @Override
    public String toString() {
        return String.valueOf((char) ('A' + row)) + (column + 1);
    }
}
