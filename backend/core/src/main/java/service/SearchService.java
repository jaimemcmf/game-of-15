package service;

import model.SearchProblem;
import model.SearchResult;
import model.SearchProgress;
import solver.Solver;

public class SearchService {

    public boolean isUnsolvable(SearchProblem problem) {
        return !SolvabilityService.isSolvable(
                problem.initialState(),
                problem.goalState());
    }

    public SearchResult solve(
            Solver solver,
            SearchProblem problem,
            SearchProgress progress) {

        if (isUnsolvable(problem)) {
            return SearchResult.unsolvable();
        }

        return solver.solve(problem, progress);
    }
}