package solver;

import model.*;
import util.SearchUtils;
import heuristic.Heuristic;

import java.util.*;

public class IDAStar implements Solver {

  private final Heuristic heuristic;

  private int expandedNodes;
  private List<Move> solutionPath;
  private boolean found;

  public IDAStar(Heuristic heuristic) {
    this.heuristic = heuristic;
  }

  @Override
  public SearchResult solve(SearchProblem problem) {

    long startTime = System.currentTimeMillis();

    if (problem.initialState().equals(problem.goalState())) {
      return new SearchResult(new ArrayList<>(), 0, true, 1, 0, false);
    }

    PuzzleState start = problem.initialState();
    PuzzleState goal = problem.goalState();

    int bound = heuristic.estimate(start);

    expandedNodes = 0;
    found = false;
    solutionPath = new ArrayList<>();

    while (!found) {

      int t = search(
        new SearchNode(start, null, 0, 0, null),
        goal,
        bound,
        new HashSet<>(),
        new ArrayList<>()
      );

      if (found) {
        return new SearchResult(
          solutionPath,
          solutionPath.size(),
          true,
          expandedNodes,
          System.currentTimeMillis() - startTime,
          false
        );
      }

      if (t == Integer.MAX_VALUE) {
        return new SearchResult(
          new ArrayList<>(),
          -1,
          false,
          expandedNodes,
          System.currentTimeMillis() - startTime,
          false
        );
      }

      bound = t;
    }

    return new SearchResult(
      new ArrayList<>(),
      -1,
      false,
      expandedNodes,
      System.currentTimeMillis() - startTime,
      false
    );
  }

  private int search(
    SearchNode node,
    PuzzleState goal,
    int bound,
    Set<PuzzleState> path,
    List<Move> moves
  ) {

    PuzzleState state = node.getState();

    int g = node.getDepth();
    int f = g + heuristic.estimate(state);

    expandedNodes++;

    if (f > bound) {
      return f;
    }

    if (state.equals(goal)) {
      solutionPath = new ArrayList<>(moves);
      found = true;
      return f;
    }

    path.add(state);

    int min = Integer.MAX_VALUE;

    Map<Move, PuzzleState> neighbors = state.getNextStatesWithMoves();

    for (Map.Entry<Move, PuzzleState> entry : neighbors.entrySet()) {

      Move move = entry.getKey();
      PuzzleState next = entry.getValue();

      if (path.contains(next)) {
        continue;
      }

      moves.add(move);

      SearchNode child = new SearchNode(next, node, 0, g + 1, move);

      int t = search(child, goal, bound, path, moves);

      if (found) return t;

      if (t < min) {
        min = t;
      }

      moves.remove(moves.size() - 1);
    }

    path.remove(state);

    return min;
  }

  @Override
  public String getName() {
    return "IDA*";
  }
}