package gic.codingtest.sudoku.ui;

import gic.codingtest.sudoku.domain.Position;

public final class CommandParser {

    public Command parse(String input) {
        if (input == null || input.isBlank()) {
            return new InvalidCommand("Command cannot be empty.");
        }

        String trimmed = input.trim();

        if (trimmed.equalsIgnoreCase("hint")) {
            return new HintCommand();
        }
        if (trimmed.equalsIgnoreCase("check")) {
            return new CheckCommand();
        }
        if (trimmed.equalsIgnoreCase("quit")) {
            return new QuitCommand();
        }

        String[] parts = trimmed.split("\\s+");
        if (parts.length != 2) {
            return new InvalidCommand(
                    "Use A3 4, C5 clear, hint, check, or quit."
            );
        }

        final Position position;
        try {
            position = Position.parse(parts[0]);
        } catch (IllegalArgumentException e) {
            return new InvalidCommand(e.getMessage());
        }

        if (parts[1].equalsIgnoreCase("clear")) {
            return new ClearCommand(position);
        }

        try {
            int value = Integer.parseInt(parts[1]);
            if (value < 1 || value > 9) {
                return new InvalidCommand(
                        "Number must be between 1 and 9."
                );
            }
            return new MoveCommand(position, value);
        } catch (NumberFormatException e) {
            return new InvalidCommand(
                    "Number must be between 1 and 9."
            );
        }
    }
}
