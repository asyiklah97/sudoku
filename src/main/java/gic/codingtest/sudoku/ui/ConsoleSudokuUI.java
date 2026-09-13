package gic.codingtest.sudoku.ui;

import gic.codingtest.sudoku.service.GameResult;
import gic.codingtest.sudoku.service.SudokuGame;
import gic.codingtest.sudoku.domain.SudokuBoard;

import java.util.Scanner;

public final class ConsoleSudokuUI {
    private final SudokuGame game;
    private final CommandParser parser;

    public ConsoleSudokuUI(SudokuGame game, CommandParser parser) {
        this.game = game;
        this.parser = parser;
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to Sudoku!");
        System.out.println();
        System.out.println("Here is your puzzle:");
        printBoard(game.board());

        while (true) {
            System.out.println();
            System.out.print(
                    "Enter command (e.g., A3 4, C5 clear, hint, check, quit): "
            );

            if (!scanner.hasNextLine()) {
                break;
            }

            String input = scanner.nextLine();
            Command command = parser.parse(input);
            GameResult result = game.execute(command);

            System.out.println(result.message());

            if (result.quit()) {
                break;
            }

            if (result.completed()) {
                System.out.println();
                printBoard(game.board());
                break;
            }

            System.out.println();
            System.out.println("Current grid:");
            printBoard(game.board());
        }
    }

    private void printBoard(SudokuBoard board) {
        int[][] values = board.toArray();

        System.out.println("    1 2 3 4 5 6 7 8 9");

        for (int row = 0; row < 9; row++) {
            System.out.print("  " + (char) ('A' + row) + " ");

            for (int col = 0; col < 9; col++) {
                System.out.print(
                        values[row][col] == 0 ? "_" : values[row][col]
                );

                if (col < 8) {
                    System.out.print(" ");
                }
            }

            System.out.println();
        }
    }
}
