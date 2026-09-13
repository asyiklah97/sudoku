package gic.codingtest.sudoku.ui;

import gic.codingtest.sudoku.domain.Position;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CommandParserTest {
    private final CommandParser parser = new CommandParser();

    @Test
    void parsesMove() {
        Command command = parser.parse("B3 7");

        assertEquals(
                new MoveCommand(
                        Position.parse("B3"), 7),
                command
        );
    }

    @Test
    void parsesClear() {
        assertEquals(
                new ClearCommand(
                        Position.parse("C5")),
                parser.parse("C5 clear")
        );
    }

    @Test
    void parsesCommandsCaseInsensitively() {
        assertInstanceOf(HintCommand.class, parser.parse("HINT"));
        assertInstanceOf(CheckCommand.class, parser.parse("Check"));
        assertInstanceOf(QuitCommand.class, parser.parse("QUIT"));
    }

    @Test
    void rejectsInvalidNumber() {
        assertInstanceOf(InvalidCommand.class, parser.parse("A3 10"));
    }
}
