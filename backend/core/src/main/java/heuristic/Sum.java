package heuristic;

import model.PuzzleState;

public class Sum implements Heuristic {
  @Override
  public int estimate(PuzzleState state) {
    byte[] currentBoard = state.getBoard();
    byte[] goalBoard = PuzzleState.defaultGoal().getBoard();
    
    int mismatches = 0;
    for (int i = 0; i < 16; i++) {
      if (currentBoard[i] != goalBoard[i]) {
        mismatches++;
      }
    }
    
    return mismatches;
  }
}
