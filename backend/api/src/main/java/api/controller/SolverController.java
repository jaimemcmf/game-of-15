package api.controller;

import model.SearchResult;

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

    @PostMapping ("/solver")
    public SolveResponseDto solve(@RequestBody SolveRequestDto request) {
        
        SearchResult result =
                service.solve(
                        request.searchAlgorithm(),
                        request.heuristic(),
                        request.initialState()
                );

        return new SolveResponseDto(
                result.solved(),
                result.depth(),
                result.path()
                        .stream()
                        .map(Enum::name)
                        .toList()
        );
    }
}