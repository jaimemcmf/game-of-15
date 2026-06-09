package api.controller;

import model.SearchResult;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.web.bind.annotation.*;

import api.dto.*;
import api.service.SolverService;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api")
public class SolverController {

        private final SolverService service;

        public SolverController(SolverService service) {
                this.service = service;
        }

        @PostMapping("/solver")
        public SolveResponseDto solve(@RequestBody SolveRequestDto request) {

                SearchResult result = service.solve(
                                request.searchAlgorithm(),
                                request.heuristic(),
                                request.initialState());

                return new SolveResponseDto(
                                request.searchAlgorithm(),
                                request.heuristic(),
                                result.solved(),
                                result.path()
                                                .stream()
                                                .map(Enum::name)
                                                .toList(),
                                result.depth(),
                                result.nodesExpanded(),
                                result.timeMs(),
                                result.timedOut());
        }

        @PostMapping("/compare")
        public CompareResponseDto compare(@RequestBody CompareRequestDto request) {

                List<AlgorithmConfig> configs = List.of(
                                new AlgorithmConfig("DFS", null),
                                new AlgorithmConfig("IDFS", null),
                                new AlgorithmConfig("BFS", null),
                                new AlgorithmConfig("Greedy", "Sum"),
                                new AlgorithmConfig("Greedy", "Manhattan"),
                                new AlgorithmConfig("Greedy", "ManhattanLinearConflict"),
                                new AlgorithmConfig("AStar", "Sum"),
                                new AlgorithmConfig("AStar", "Manhattan"),
                                new AlgorithmConfig("AStar", "ManhattanLinearConflict"),
                                new AlgorithmConfig("IDAStar", "Sum"),
                                new AlgorithmConfig("IDAStar", "Manhattan"),
                                new AlgorithmConfig("IDAStar", "ManhattanLinearConflict"));

                ExecutorService executor = Executors.newFixedThreadPool(configs.size());

                List<CompletableFuture<SolveResponseDto>> futures = configs.stream()
                                .map(cfg -> CompletableFuture.supplyAsync(() -> {

                                        try {
                                                SearchResult result = service.solve(
                                                                cfg.name(),
                                                                cfg.heuristic(),
                                                                request.initialState());

                                                return new SolveResponseDto(
                                                                cfg.name(),
                                                                cfg.heuristic(),
                                                                result.solved(),
                                                                result.path().stream().map(Enum::name).toList(),
                                                                result.depth(),
                                                                result.nodesExpanded(),
                                                                result.timeMs(),
                                                                result.timedOut());

                                        } catch (Exception e) {
                                                // return a "failed" result instead of failing the whole pipeline
                                                return new SolveResponseDto(
                                                                cfg.name(),
                                                                cfg.heuristic(),
                                                                false,
                                                                List.of(),
                                                                -1,
                                                                -1,
                                                                -1,
                                                                true);
                                        }

                                }, executor))
                                .toList();

                CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

                List<SolveResponseDto> results = futures.stream()
                                .map(CompletableFuture::join)
                                .toList();

                return new CompareResponseDto(results);
        }
}