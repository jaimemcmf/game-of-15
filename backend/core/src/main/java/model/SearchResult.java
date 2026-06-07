package model;

import java.util.List;

public record SearchResult(
        List<Move> path,
        int depth,
        boolean solved
) {
    public static SearchResult unsolvable() {
        return new SearchResult(List.of(), 0, false);
    }
}
