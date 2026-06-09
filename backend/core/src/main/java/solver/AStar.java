package solver;

import model.*;
import util.SearchUtils;
import heuristic.Heuristic;

import java.util.*;

public class AStar implements Solver {
  private Heuristic heuristic;

  public AStar(Heuristic heuristic) {
    this.heuristic = heuristic;
  }

  @Override
  public SearchResult solve(SearchProblem problem) {

    if (problem.initialState().equals(problem.goalState())) {
      return new SearchResult(new ArrayList<>(), 0, true, 1, 0, false);
    }

    long startTime = System.currentTimeMillis();

    // Priority queue ordered by f = g + h
    PriorityQueue<SearchNode> openSet = new PriorityQueue<>(
        Comparator.comparingInt(node -> node.getDepth() + heuristic.estimate(node.getState())));

    // Best known cost to reach each state
    Map<PuzzleState, Integer> bestG = new HashMap<>();

    SearchNode start = new SearchNode(
        problem.initialState(),
        null,
        0,
        0,
        null);

    openSet.add(start);
    bestG.put(problem.initialState(), 0);

    int expandedNodes = 0;

    while (!openSet.isEmpty()) {

      SearchNode current = openSet.poll();
      expandedNodes++;

      // If this is a stale node (worse than already found path), skip it
      int currentG = current.getDepth();
      if (currentG > bestG.getOrDefault(current.getState(), Integer.MAX_VALUE)) {
        continue;
      }

      if (current.getState().equals(problem.goalState())) {
        List<Move> path = SearchUtils.reconstructPath(current);

        return new SearchResult(
            path,
            path.size(),
            true,
            expandedNodes,
            System.currentTimeMillis() - startTime,
            false);
      }

      Map<Move, PuzzleState> neighbors = current.getState().getNextStatesWithMoves();

      for (Map.Entry<Move, PuzzleState> entry : neighbors.entrySet()) {

        Move move = entry.getKey();
        PuzzleState nextState = entry.getValue();

        int newG = currentG + 1;

        // Only proceed if this path is better
        if (newG < bestG.getOrDefault(nextState, Integer.MAX_VALUE)) {

          bestG.put(nextState, newG);

          SearchNode nextNode = new SearchNode(
              nextState,
              current,
              0,
              newG,
              move);

          openSet.add(nextNode);
        }
      }

      checkForInterruption();
    }

    return new SearchResult(
        new ArrayList<>(),
        -1,
        false,
        expandedNodes,
        System.currentTimeMillis() - startTime,
        false);
  }

  @Override
  public String getName() {
    return "A*";
  }
}