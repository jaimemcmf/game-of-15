package api.service;

import model.*;
import solver.*;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import heuristic.ManhattanDistance;
import heuristic.Sum;

import java.util.concurrent.*;

@Service
public class SolverService {

    private final ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    public SearchResult solve(
            String algorithm,
            String heuristic,
            byte[] initialBoard) {

        Solver solver = createSolver(algorithm, heuristic);
        PuzzleState initial = new PuzzleState(initialBoard);
        SearchProblem problem = new SearchProblem(initial);

        Future<SearchResult> future = executor.submit(() ->
                solver.solve(problem)
        );

        try {
            return future.get(5, TimeUnit.SECONDS);
            
        } catch (TimeoutException e) {

            future.cancel(true); // attempt to stop execution

            return SearchResult.timeout();

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

            case "astar" -> new AStar(
                    switch (heuristic.toLowerCase()) {

                        case "manhattan" -> new ManhattanDistance();

                        case "sum" -> new Sum();

                        default -> throw new IllegalArgumentException(
                                "Unknown heuristic: " + heuristic
                        );
                    }
            );

            case "greedy" -> new Greedy(
                    switch (heuristic.toLowerCase()) {

                        case "manhattan" -> new ManhattanDistance();

                        case "sum" -> new Sum();

                        default -> throw new IllegalArgumentException(
                                "Unknown heuristic: " + heuristic
                        );
                    }
            );

            default -> throw new IllegalArgumentException(
                    "Unknown algorithm: " + algorithm
            );
        };
    }
}