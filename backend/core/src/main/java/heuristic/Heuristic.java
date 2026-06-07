package heuristic;
import model.PuzzleState;

public interface Heuristic {
  int estimate(PuzzleState state);
}
