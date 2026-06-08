package api.dto;

import java.util.List;

public record CompareResponseDto(
    List<SolveResponseDto> results
) {}
