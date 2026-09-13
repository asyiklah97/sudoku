package gic.codingtest.sudoku.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SudokuBoardTest {

    @Test
    void protectsGivenCells() {
        SudokuBoard board = SudokuBoard.from(new int[][]{
                {5, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0}
        });

        assertThrows(
                FixedCellException.class,
                () -> board.set(Position.parse("A1"), 6)
        );
    }

    @Test
    void allowsChangingNonFixedCell() {
        SudokuBoard board = SudokuBoard.empty();
        Position position = Position.parse("A1");

        board.set(position, 7);

        assertEquals(7, board.get(position));

        board.clear(position);

        assertEquals(0, board.get(position));
    }

    @Test
    void rejectsValuesOutsideOneToNine() {
        SudokuBoard board = SudokuBoard.empty();

        assertThrows(
                IllegalArgumentException.class,
                () -> board.set(Position.parse("A1"), 10)
        );
    }
}
