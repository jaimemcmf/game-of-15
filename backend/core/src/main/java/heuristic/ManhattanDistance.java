package heuristic;

import model.PuzzleState;

public class ManhattanDistance implements Heuristic {
  @Override
  public int estimate(PuzzleState state) {
    byte[] board = state.getBoard();
    int totalDistance = 0;
    
    for (int i = 0; i < 16; i++) {
      byte tile = board[i];
      
      if (tile == 0) continue; // Skip the blank
      
      // Goal position for tile with value `tile` is at index `tile - 1`
      int goalPos = tile - 1;
      
      // Calculate current position in grid
      int currentRow = i / 4;
      int currentCol = i % 4;
      
      // Calculate goal position in grid
      int goalRow = goalPos / 4;
      int goalCol = goalPos % 4;
      
      // Manhattan distance is sum of absolute differences
      totalDistance += Math.abs(currentRow - goalRow) + Math.abs(currentCol - goalCol);
    }
    
    return totalDistance;
  }
}
