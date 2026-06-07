package util;

import java.util.LinkedList;
import java.util.List;

import model.Move;
import model.SearchNode;

public final class SearchUtils {

    private SearchUtils() {}

    public static List<Move> reconstructPath(
            SearchNode goalNode) {

        LinkedList<Move> path =
                new LinkedList<>();

        SearchNode current = goalNode;

        while (current.getParent() != null) {

            path.addFirst(current.getMove());

            current = current.getParent();
        }

        return path;
    }
}