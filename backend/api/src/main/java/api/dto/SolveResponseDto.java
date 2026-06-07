package api.dto;

import java.util.List;

public record SolveResponseDto(
    boolean solved,
    int depth,
    List<String> moves
) {}
