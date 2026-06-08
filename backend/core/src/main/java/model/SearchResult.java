package model;

import java.util.List;

public record SearchResult(
        List<Move> path,
        int depth,
        boolean solved,
        int nodesExpanded,
        long timeMs
) {
    public static SearchResult unsolvable() {
        return new SearchResult(List.of(), 0, false, 0, 0);
    }
    public static SearchResult timeout() {
        return new SearchResult(List.of(), 0, false, 0, 0);
    }
}
