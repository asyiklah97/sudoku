package gic.codingtest.sudoku.service;

public record GameResult(String message, boolean completed, boolean quit) {
    public static GameResult message(String message) {
        return new GameResult(message, false, false);
    }

    public static GameResult completed(String message) {
        return new GameResult(message, true, false);
    }

    public static GameResult quitResult() {
        return new GameResult("Goodbye!", false, true);
    }
}
