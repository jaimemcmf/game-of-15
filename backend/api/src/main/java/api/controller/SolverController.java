package api.controller;

import model.SearchResult;

import java.util.ArrayList;
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

        public SolverController(
                        SolverService service) {

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
                                result.solved(),
                                result.path()
                                                .stream()
                                                .map(Enum::name)
                                                .toList(),
                                result.depth(),
                                result.nodesExpanded(),
                                result.timeMs(),
                                false);
        }

        @PostMapping("/compare")
        public CompareResponseDto compare(@RequestBody CompareRequestDto request) {

                ExecutorService executor = Executors.newFixedThreadPool(5);

                List<AlgorithmConfig> configs = List.of(
                                new AlgorithmConfig("bfs", null),
                                new AlgorithmConfig("dfs", null),
                                new AlgorithmConfig("idfs", null),
                                new AlgorithmConfig("greedy", "sum"),
                                new AlgorithmConfig("greedy", "sum"),
                                new AlgorithmConfig("astar", "manhattan"),
                                new AlgorithmConfig("astar", "manhattan"));

                List<CompletableFuture<SolveResponseDto>> futures = configs.stream()
                                .map(cfg -> CompletableFuture.supplyAsync(() -> {

                                        try {
                                                SearchResult result = service.solve(
                                                                cfg.name(),
                                                                cfg.heuristic(),
                                                                request.initialState());

                                                return new SolveResponseDto(
                                                                cfg.name(),
                                                                result.solved(),
                                                                result.path().stream().map(Enum::name).toList(),
                                                                result.depth(),
                                                                result.nodesExpanded(),
                                                                result.timeMs(),
                                                                false);

                                        } catch (Exception e) {
                                                // return a "failed" result instead of failing the whole pipeline
                                                return new SolveResponseDto(
                                                                cfg.name(),
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