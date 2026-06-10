package solver;

import model.*;
import util.SearchUtils;

import java.util.*;

public class IDFS implements Solver {

  @Override
  public SearchResult solve(SearchProblem problem, SearchProgress progress) {
    long startTime = System.currentTimeMillis();

    if (problem.initialState().equals(problem.goalState())) {
      progress.setExpandedNodes(1);
      return new SearchResult(
          new ArrayList<>(),
          0,
          true,
          1,
          System.currentTimeMillis() - startTime,
          false
      );
    }

    int maxDepth = 1;
    int totalExpanded = 0;

    while (true) {
      SearchResult result = depthLimitedSearch(problem, maxDepth, progress, totalExpanded);

      // update global progress counter from last run
      totalExpanded = progress.getExpandedNodes();

      if (result.solved()) {
        return result;
      }

      maxDepth++;
    }
  }

  private SearchResult depthLimitedSearch(
      SearchProblem problem,
      int maxDepth,
      SearchProgress progress,
      int globalExpandedCount
  ) {

    Stack<SearchNode> stack = new Stack<>();
    Set<PuzzleState> visited = new HashSet<>();

    long startTime = System.currentTimeMillis();

    SearchNode initialNode = new SearchNode(
        problem.initialState(),
        null,
        0,
        0,
        null
    );

    stack.push(initialNode);
    visited.add(problem.initialState());

    while (!stack.isEmpty()) {

      SearchNode currentNode = stack.pop();
      globalExpandedCount++;
      progress.setExpandedNodes(globalExpandedCount);

      if (currentNode.getDepth() > maxDepth) {
        continue;
      }

      Map<Move, PuzzleState> nextStates =
          currentNode.getState().getNextStatesWithMoves();

      for (Map.Entry<Move, PuzzleState> entry : nextStates.entrySet()) {

        Move move = entry.getKey();
        PuzzleState nextState = entry.getValue();

        if (currentNode.getDepth() + 1 > maxDepth) {
          continue;
        }

        if (!visited.contains(nextState)) {
          visited.add(nextState);

          SearchNode nextNode = new SearchNode(
              nextState,
              currentNode,
              0,
              currentNode.getDepth() + 1,
              move
          );

          if (nextState.equals(problem.goalState())) {
            List<Move> path = SearchUtils.reconstructPath(nextNode);

            return new SearchResult(
                path,
                path.size(),
                true,
                globalExpandedCount,
                System.currentTimeMillis() - startTime,
                false
            );
          }

          stack.push(nextNode);
        }
      }

      checkForInterruption();
    }

    return new SearchResult(
        new ArrayList<>(),
        -1,
        false,
        globalExpandedCount,
        System.currentTimeMillis() - startTime,
        false
    );
  }

  @Override
  public String getName() {
    return "IDFS";
  }
}