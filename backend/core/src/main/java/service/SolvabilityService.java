package service;

import model.PuzzleState;

public final class SolvabilityService {

    private SolvabilityService() {}

    public static boolean isSolvable(
            PuzzleState initial,
            PuzzleState goal) {

        return standardSolvable(initial)
                == standardSolvable(goal);
    }

    private static boolean standardSolvable(
            PuzzleState state) {

        return (countInversions(state) % 2 == 0)
                == isBlankOnOddRowFromBottom(state);
    }

    private static int countInversions(
            PuzzleState state) {

        int inversions = 0;

        byte[] board = state.getBoard();

        for (int i = 0; i < board.length; i++) {

            if (board[i] == 0) {
                continue;
            }

            for (int j = i + 1; j < board.length; j++) {

                if (board[j] == 0) {
                    continue;
                }

                if (board[i] > board[j]) {
                    inversions++;
                }
            }
        }

        return inversions;
    }

    private static boolean isBlankOnOddRowFromBottom(
            PuzzleState state) {

        int blankIndex = findBlankIndex(state);

        int rowFromTop = blankIndex / 4;

        int rowFromBottom = 4 - rowFromTop;

        return rowFromBottom % 2 == 1;
    }

    private static int findBlankIndex(
            PuzzleState state) {

        byte[] board = state.getBoard();

        for (int i = 0; i < board.length; i++) {

            if (board[i] == 0) {
                return i;
            }
        }

        throw new IllegalStateException(
                "Puzzle state contains no blank tile");
    }
}