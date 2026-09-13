package gic.codingtest.sudoku.domain;

public record SudokuPuzzle(int[][] givens, int[][] solution) {
    public SudokuPuzzle {
        givens = copy(givens);
        solution = copy(solution);
    }

    public int numberOfGivenCells() {
        int count = 0;
        for (int[] row : givens) {
            for (int value : row) {
                if (value != 0) {
                    count++;
                }
            }
        }
        return count;
    }

    public int[][] givens() {
        return copy(givens);
    }

    public int[][] solution() {
        return copy(solution);
    }

    private static int[][] copy(int[][] source) {
        int[][] result = new int[source.length][];
        for (int i = 0; i < source.length; i++) {
            result[i] = source[i].clone();
        }
        return result;
    }
}
