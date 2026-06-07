package api.dto;

public record SolveRequestDto(
    String searchAlgorithm,
    String heuristic,
    byte[] initialState
) {}
