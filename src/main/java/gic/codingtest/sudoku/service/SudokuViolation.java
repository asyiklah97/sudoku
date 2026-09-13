package gic.codingtest.sudoku.service;

public record SudokuViolation(int number, ViolationType type, int index) {
    @Override
    public String toString() {
        return switch (type) {
            case ROW -> "Number " + number + " already exists in Row "
                    + (char) ('A' + index) + ".";
            case COLUMN -> "Number " + number + " already exists in Column "
                    + (index + 1) + ".";
            case SUBGRID -> "Number " + number
                    + " already exists in the same 3×3 subgrid.";
        };
    }
}
