package model;

public class SearchNode {

  private final PuzzleState state;
  private final SearchNode parent;
  private final int cost;
  private final int depth;
  private final Move move;
  
  public SearchNode(PuzzleState state, SearchNode parent, int cost, int depth, Move move) {
    this.state = state;
    this.parent = parent;
    this.cost = cost;
    this.depth = depth;
    this.move = move;
  }
  
  public PuzzleState getState() {
    return state;
  }
  
  public SearchNode getParent() {
    return parent;
  }
  
  public int getCost() {
    return cost;
  }
  
  public int getDepth() {
    return depth;
  }
  
  public Move getMove() {
    return move;
  }
}
