export interface SolveResponse {
  solved: boolean;
  moves: string[];
  depth: number;
  nodesExpanded: number;
  timeMs: number;
}