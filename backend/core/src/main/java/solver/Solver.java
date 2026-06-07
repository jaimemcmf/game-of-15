package solver;

import model.SearchProblem;
import model.SearchResult;

public interface Solver {
  
  SearchResult solve(SearchProblem problem);

  String getName();

  default void checkForInterruption() {
    if (Thread.currentThread().isInterrupted()) {
      throw new RuntimeException("Search was interrupted");
    }
  }
}
