package model;

public record SearchProblem(
    PuzzleState initialState
) {
    public PuzzleState goalState() {
        return PuzzleState.defaultGoal();
    }
}
