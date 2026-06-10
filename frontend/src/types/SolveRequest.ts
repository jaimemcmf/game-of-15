export type Algorithm = "BFS" | "DFS" | "AStar" | "Greedy" | "IDFS" | "IDAStar";

export type Heuristic = "Manhattan" | "Sum" | "ManhattanLinearConflict";

export type SolveRequest = {
  searchAlgorithm: Algorithm;
  heuristic?: Heuristic;
  initialState: number[];
  timeOutLimit: Number;
}