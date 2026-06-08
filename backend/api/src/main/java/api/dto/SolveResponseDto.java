package api.dto;

import java.util.List;

public record SolveResponseDto(
    String algorithm,
    boolean solved,
    List<String> moves,
    int depth,
    int nodesExpanded,
    long timeMs,
    boolean timeout
) {}
