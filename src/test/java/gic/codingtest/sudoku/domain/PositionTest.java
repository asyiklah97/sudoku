package gic.codingtest.sudoku.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PositionTest {

    @Test
    void parsesCell() {
        Position position = Position.parse("B3");

        assertEquals(1, position.row());
        assertEquals(2, position.column());
        assertEquals("B3", position.toString());
    }

    @Test
    void acceptsLowerCaseRow() {
        assertEquals(new Position(2, 4), Position.parse("c5"));
    }

    @Test
    void rejectsInvalidCell() {
        assertThrows(
                IllegalArgumentException.class,
                () -> Position.parse("J3")
        );
    }
}
