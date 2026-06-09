export type Result = {
  algorithm: string;
  heuristic?: string;
  moves: string[];
  nodesExpanded: number;
  depth: number;
  timeMs: number;
  timedOut: boolean;
};