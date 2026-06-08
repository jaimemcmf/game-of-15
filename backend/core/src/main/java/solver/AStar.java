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
    
    PriorityQueue<SearchNode> queue = new PriorityQueue<>((node1, node2) -> {
      int f1 = node1.getDepth() + heuristic.estimate(node1.getState());
      int f2 = node2.getDepth() + heuristic.estimate(node2.getState());
      return Integer.compare(f1, f2);
    });
    Set<PuzzleState> visited = new HashSet<>();
    
    SearchNode initialNode = new SearchNode(problem.initialState(), null, 0, 0, null);
    queue.add(initialNode);
    visited.add(problem.initialState());
    
    while (!queue.isEmpty()) {
      SearchNode currentNode = queue.poll();
      
      Map<Move, PuzzleState> nextStatesWithMoves = currentNode.getState().getNextStatesWithMoves();
      
      for (Map.Entry<Move, PuzzleState> entry : nextStatesWithMoves.entrySet()) {
        Move move = entry.getKey();
        PuzzleState nextState = entry.getValue();
        
        if (!visited.contains(nextState)) {
          visited.add(nextState);
          SearchNode nextNode = new SearchNode(nextState, currentNode, 0, currentNode.getDepth() + 1, move);
          
          if (nextState.equals(problem.goalState())) {
            List<Move> path = SearchUtils.reconstructPath(nextNode);
            return new SearchResult(path, path.size(), true, visited.size(), System.currentTimeMillis() - startTime, false);
          }
          
          queue.add(nextNode);
        }
      }
      checkForInterruption();
    }
    
    return new SearchResult(new ArrayList<>(), -1, false, visited.size(), System.currentTimeMillis() - startTime, false);
  }

  @Override
  public String getName() {
    return "A*";
  }
}
