package heuristic;

import model.PuzzleState;

public class ManhattanLinearConflict implements Heuristic {

  @Override
  public int estimate(PuzzleState state) {
    byte[] board = state.getBoard();
    int totalDistance = 0;

    // Manhattan distance
    for (int i = 0; i < 16; i++) {
      byte tile = board[i];

      if (tile == 0) continue;

      int goalPos = tile - 1;

      int currentRow = i / 4;
      int currentCol = i % 4;

      int goalRow = goalPos / 4;
      int goalCol = goalPos % 4;

      totalDistance += Math.abs(currentRow - goalRow)
                      + Math.abs(currentCol - goalCol);
    }

    // Linear conflict (rows)
    totalDistance += rowLinearConflict(board);

    // Linear conflict (columns)
    totalDistance += colLinearConflict(board);

    return totalDistance;
  }

  private int rowLinearConflict(byte[] board) {
    int conflict = 0;

    for (int row = 0; row < 4; row++) {
      for (int i = 0; i < 4; i++) {
        int index1 = row * 4 + i;
        byte tile1 = board[index1];
        if (tile1 == 0) continue;

        int goalRow1 = (tile1 - 1) / 4;
        int goalCol1 = (tile1 - 1) % 4;

        if (goalRow1 != row) continue;

        for (int j = i + 1; j < 4; j++) {
          int index2 = row * 4 + j;
          byte tile2 = board[index2];
          if (tile2 == 0) continue;

          int goalRow2 = (tile2 - 1) / 4;
          int goalCol2 = (tile2 - 1) % 4;

          if (goalRow2 != row) continue;

          // conflict if reversed relative to goal order
          if (goalCol1 > goalCol2) {
            conflict += 2;
          }
        }
      }
    }

    return conflict;
  }

  private int colLinearConflict(byte[] board) {
    int conflict = 0;

    for (int col = 0; col < 4; col++) {
      for (int i = 0; i < 4; i++) {
        int index1 = i * 4 + col;
        byte tile1 = board[index1];
        if (tile1 == 0) continue;

        int goalRow1 = (tile1 - 1) / 4;
        int goalCol1 = (tile1 - 1) % 4;

        if (goalCol1 != col) continue;

        for (int j = i + 1; j < 4; j++) {
          int index2 = j * 4 + col;
          byte tile2 = board[index2];
          if (tile2 == 0) continue;

          int goalRow2 = (tile2 - 1) / 4;
          int goalCol2 = (tile2 - 1) % 4;

          if (goalCol2 != col) continue;

          // conflict if reversed relative to goal order
          if (goalRow1 > goalRow2) {
            conflict += 2;
          }
        }
      }
    }

    return conflict;
  }
}