export interface SolveResponse {
  algorithm: string;
  heuristic: string;
  solved: boolean;
  moves: string[];
  depth: number;
  nodesExpanded: number;
  timeMs: number;
}