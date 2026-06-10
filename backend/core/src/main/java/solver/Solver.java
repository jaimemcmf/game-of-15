package solver;

import model.SearchProblem;
import model.SearchResult;
import model.SearchProgress;

public interface Solver {
  
  SearchResult solve(SearchProblem problem, SearchProgress progress);

  String getName();

  default void checkForInterruption() {
    if (Thread.currentThread().isInterrupted()) {
      throw new RuntimeException("Search was interrupted");
    }
  }
}
