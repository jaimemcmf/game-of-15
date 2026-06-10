package api.service;

import model.*;
import solver.*;

import org.springframework.stereotype.Service;

import heuristic.ManhattanDistance;
import heuristic.Sum;

import java.util.concurrent.*;

@Service
public class SolverService {

    private final ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    public SearchResult solve(
            String algorithm,
            String heuristic,
            byte[] initialBoard,
            int timeOutLimit) {

        Solver solver = createSolver(algorithm, heuristic);
        PuzzleState initial = new PuzzleState(initialBoard);
        SearchProblem problem = new SearchProblem(initial);
        SearchProgress progress = new SearchProgress();

        Future<SearchResult> future = executor.submit(() -> solver.solve(problem, progress));

        try {
            return future.get(timeOutLimit, TimeUnit.SECONDS);
        } catch (TimeoutException e) {

            int expandedNodes = progress.getExpandedNodes();

            future.cancel(true); // attempt to stop execution

            return SearchResult.timedOutSearchResult(expandedNodes);

        } catch (ExecutionException e) {

            throw new RuntimeException(e.getCause());

        } catch (InterruptedException e) {

            Thread.currentThread().interrupt();

            throw new RuntimeException("Search interrupted", e);
        }
    }

    private Solver createSolver(String algorithm, String heuristic) {

        return switch (algorithm.toLowerCase()) {

            case "bfs" -> new BFS();

            case "dfs" -> new DFS();

            case "idfs" -> new IDFS();

            case "greedy" -> new Greedy(
                    switch (heuristic.toLowerCase()) {

                        case "manhattan" -> new ManhattanDistance();

                        case "sum" -> new Sum();

                        case "manhattanlinearconflict" -> new heuristic.ManhattanLinearConflict();

                        default -> throw new IllegalArgumentException(
                                "Unknown heuristic: " + heuristic);
                    });

            case "astar" -> new AStar(
                    switch (heuristic.toLowerCase()) {

                        case "manhattan" -> new ManhattanDistance();

                        case "sum" -> new Sum();

                        case "manhattanlinearconflict" -> new heuristic.ManhattanLinearConflict();

                        default -> throw new IllegalArgumentException(
                                "Unknown heuristic: " + heuristic);
                    });

            case "idastar" -> new IDAStar(
                    switch (heuristic.toLowerCase()) {

                        case "manhattan" -> new ManhattanDistance();

                        case "sum" -> new Sum();

                        case "manhattanlinearconflict" -> new heuristic.ManhattanLinearConflict();

                        default -> throw new IllegalArgumentException(
                                "Unknown heuristic: " + heuristic);
                    });

            default -> throw new IllegalArgumentException(
                    "Unknown algorithm: " + algorithm);
        };
    }
}