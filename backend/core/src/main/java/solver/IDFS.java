package solver;

import model.*;
import util.SearchUtils;

import java.util.*;

public class IDFS implements Solver {
  @Override
  public SearchResult solve(SearchProblem problem) {
    long startTime = System.currentTimeMillis();
    if (problem.initialState().equals(problem.goalState())) {
      return new SearchResult(new ArrayList<>(), 0, true, 1, System.currentTimeMillis() - startTime, false);
    }
    
    int maxDepth = 1;
    while (true) {
      SearchResult result = depthLimitedSearch(problem, maxDepth);
      if (result.solved()) {
        return result;
      }
      maxDepth++;
    }
  }
  
  private SearchResult depthLimitedSearch(SearchProblem problem, int maxDepth) {
    Stack<SearchNode> stack = new Stack<>();
    Set<PuzzleState> visited = new HashSet<>();
    long startTime = System.currentTimeMillis();
    
    SearchNode initialNode = new SearchNode(problem.initialState(), null, 0, 0, null);
    stack.push(initialNode);
    visited.add(problem.initialState());
    
    while (!stack.isEmpty()) {
      SearchNode currentNode = stack.pop();
      
      // If we've exceeded the depth limit, skip this node
      if (currentNode.getDepth() > maxDepth) {
        continue;
      }
      
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
          
          // Only push if we haven't exceeded the depth limit
          if (nextNode.getDepth() <= maxDepth) {
            stack.push(nextNode);
          }
        }
      }
      checkForInterruption();
    }
    
    return new SearchResult(new ArrayList<>(), -1, false, visited.size(), System.currentTimeMillis() - startTime, false);
  }

  @Override
  public String getName() {
    return "IDFS";
  }
}
