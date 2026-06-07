export type Algorithm = "bfs" | "dfs" | "astar" | "greedy" | "idfs";

export type Heuristic = "manhattan" | "sum";

export type SolveRequest = {
  searchAlgorithm: Algorithm;
  heuristic?: Heuristic;
  initialState: number[];
}