package gic.codingtest.sudoku.service;

import gic.codingtest.sudoku.domain.FixedCellException;
import gic.codingtest.sudoku.domain.Position;
import gic.codingtest.sudoku.domain.SudokuBoard;
import gic.codingtest.sudoku.domain.SudokuPuzzle;
import gic.codingtest.sudoku.generator.SudokuGenerator;
import gic.codingtest.sudoku.ui.*;

import java.util.List;
import java.util.Optional;
import java.util.Random;

public final class SudokuGame {
    private final SudokuGenerator generator;
    private final SudokuValidator validator;
    private final SudokuSolver solver;
    private final Random random;

    private SudokuPuzzle puzzle;
    private SudokuBoard board;

    public SudokuGame(
            SudokuGenerator generator,
            SudokuValidator validator,
            SudokuSolver solver) {
        this(generator, validator, solver, new Random());
    }

    public SudokuGame(
            SudokuGenerator generator,
            SudokuValidator validator,
            SudokuSolver solver,
            Random random) {
        this.generator = generator;
        this.validator = validator;
        this.solver = solver;
        this.random = random;
        newGame();
    }

    public void newGame() {
        puzzle = generator.generate();
        board = SudokuBoard.from(puzzle.givens());
    }

    public SudokuBoard board() {
        return board.copy();
    }

    public GameResult execute(Command command) {
        return switch (command) {
            case MoveCommand move -> move(move);
            case ClearCommand clear -> clear(clear);
            case HintCommand ignored -> hint();
            case CheckCommand ignored -> check();
            case QuitCommand ignored -> GameResult.quitResult();
            case InvalidCommand invalid ->
                    GameResult.message("Invalid command. " + invalid.message());
        };
    }

    private GameResult move(MoveCommand command) {
        try {
            board.set(command.position(), command.value());
        } catch (FixedCellException e) {
            return GameResult.message("Invalid move. " + e.getMessage());
        }

        if (isCompletedCorrectly()) {
            return GameResult.completed(
                    "You have successfully completed the Sudoku puzzle!"
            );
        }

        return GameResult.message("Move accepted.");
    }

    private GameResult clear(ClearCommand command) {
        try {
            board.clear(command.position());
        } catch (FixedCellException e) {
            return GameResult.message("Invalid move. " + e.getMessage());
        }

        return GameResult.message("Cell " + command.position() + " cleared.");
    }

    private GameResult hint() {
        List<Position> empty = findEmptyCells();
        if (empty.isEmpty()) {
            return GameResult.message("There are no empty cells.");
        }

        Optional<int[][]> solved = solver.solve(board);
        if (solved.isEmpty()) {
            return GameResult.message(
                    "Cannot provide a hint because the current grid has rule violations."
            );
        }

        Position position = empty.get(random.nextInt(empty.size()));
        int value = solved.get()[position.row()][position.column()];
        board.set(position, value);

        return GameResult.message(
                "Hint: Cell " + position + " = " + value
        );
    }

    private GameResult check() {
        List<SudokuViolation> violations = validator.validate(board);

        if (violations.isEmpty()) {
            return GameResult.message("No rule violations detected.");
        }

        return GameResult.message(
                String.join(System.lineSeparator(),
                        violations.stream().map(SudokuViolation::toString).toList())
        );
    }

    private boolean isCompletedCorrectly() {
        if (!board.isComplete()) {
            return false;
        }

        Optional<int[][]> solved = solver.solve(board);
        if (solved.isEmpty()) {
            return false;
        }

        int[][] actual = board.toArray();
        int[][] solution = puzzle.solution();

        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                if (actual[row][col] != solution[row][col]) {
                    return false;
                }
            }
        }

        return true;
    }

    private List<Position> findEmptyCells() {
        int[][] values = board.toArray();
        java.util.ArrayList<Position> result = new java.util.ArrayList<>();

        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                if (values[row][col] == 0) {
                    result.add(new Position(row, col));
                }
            }
        }

        return result;
    }
}
