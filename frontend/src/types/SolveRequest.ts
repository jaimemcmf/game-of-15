export type Algorithm = "BFS" | "DFS" | "AStar" | "Greedy" | "IDFS";

export type Heuristic = "Manhattan" | "Sum";

export type SolveRequest = {
  searchAlgorithm: Algorithm;
  heuristic?: Heuristic;
  initialState: number[];
}