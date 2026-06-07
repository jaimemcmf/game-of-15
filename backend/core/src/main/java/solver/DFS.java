package solver;

import model.*;
import util.SearchUtils;

import java.util.*;

public class DFS implements Solver {
  @Override
  public SearchResult solve(SearchProblem problem) {
    if (problem.initialState().equals(problem.goalState())) {
      return new SearchResult(new ArrayList<>(), 0, true);
    }
    
    Stack<SearchNode> stack = new Stack<>();
    Set<PuzzleState> visited = new HashSet<>();
    
    SearchNode initialNode = new SearchNode(problem.initialState(), null, 0, 0, null);
    stack.push(initialNode);
    visited.add(problem.initialState());
    
    while (!stack.isEmpty()) {
      SearchNode currentNode = stack.pop();
      
      // Generate next states with their corresponding moves
      Map<Move, PuzzleState> nextStatesWithMoves = currentNode.getState().getNextStatesWithMoves();
      
      for (Map.Entry<Move, PuzzleState> entry : nextStatesWithMoves.entrySet()) {
        Move move = entry.getKey();
        PuzzleState nextState = entry.getValue();
        
        if (!visited.contains(nextState)) {
          visited.add(nextState);
          SearchNode nextNode = new SearchNode(nextState, currentNode, 0, currentNode.getDepth() + 1, move);
          
          if (nextState.equals(problem.goalState())) {
            List<Move> path = SearchUtils.reconstructPath(nextNode);
            return new SearchResult(path, path.size(), true);
          }
          
          stack.push(nextNode);
        }
      }
      checkForInterruption();
    }
    
    return new SearchResult(new ArrayList<>(), -1, false);
  }

  @Override
  public String getName() {
    return "DFS";
  }
}
