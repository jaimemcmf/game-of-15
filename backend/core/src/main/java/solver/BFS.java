package solver;

import model.*;
import util.SearchUtils;

import java.util.*;

public class BFS implements Solver {
    @Override
    public SearchResult solve(SearchProblem problem) {
        if (problem.initialState().equals(problem.goalState())) {
            return new SearchResult(new ArrayList<>(), 0, true, 1, 0);
        }
        
        Queue<SearchNode> queue = new LinkedList<>();
        Set<PuzzleState> visited = new HashSet<>();
        long startTime = System.currentTimeMillis();
        
        SearchNode initialNode = new SearchNode(problem.initialState(), null, 0, 0, null);
        queue.add(initialNode);
        visited.add(problem.initialState());
        
        while (!queue.isEmpty()) {
            SearchNode currentNode = queue.poll();
            
            // Generate next states with their corresponding moves
            Map<Move, PuzzleState> nextStatesWithMoves = currentNode.getState().getNextStatesWithMoves();
            
            for (Map.Entry<Move, PuzzleState> entry : nextStatesWithMoves.entrySet()) {
                Move move = entry.getKey();
                PuzzleState nextState = entry.getValue();
                
                if (!visited.contains(nextState)) {
                    visited.add(nextState);
                    SearchNode nextNode = new SearchNode(nextState, currentNode,0, currentNode.getDepth() + 1, move);
                    
                    if (nextState.equals(problem.goalState())) {
                        List<Move> path = SearchUtils.reconstructPath(nextNode);
                        return new SearchResult(path, path.size(), true, visited.size(), System.currentTimeMillis() - startTime);
                    }
                    
                    queue.add(nextNode);
                }
            }
            checkForInterruption();
        }
        
        return new SearchResult(new ArrayList<>(), -1, false, visited.size(), System.currentTimeMillis() - startTime);
    }

    @Override
    public String getName() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getName'");
    }
}
