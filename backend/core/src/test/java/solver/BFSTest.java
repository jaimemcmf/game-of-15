package solver;

import model.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BFSTest {

    @Test
    void solvesOneMovePuzzle() {

        PuzzleState start = new PuzzleState(new byte[]{
            1,2,3,4,
            5,6,7,8,
            9,10,11,12,
            13,14,0,15
        });

        SearchProblem problem =
                new SearchProblem(
                        start
                );

        BFS bfs = new BFS();

        SearchResult result =
                bfs.solve(problem);

        assertTrue(result.solved());
        assertEquals(1, result.depth());
    }
}