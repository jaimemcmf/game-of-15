import { useState } from "react";

import { Button } from "../../@/components/ui/button";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "../../@/components/ui/select";

import { Card } from "../../@/components/ui/card";

import type { Algorithm, Heuristic } from "@/types/SolveRequest";

type Props = {
  onSolve: (algorithm: Algorithm, heuristic?: Heuristic) => void;
  loading: boolean;
};

export function Controls({ onSolve, loading }: Props) {
  const [algorithm, setAlgorithm] = useState<Algorithm>("astar");
  const [heuristic, setHeuristic] = useState<Heuristic>("manhattan");

  const showHeuristic =
    algorithm === "astar" || algorithm === "greedy";

  return (
    <Card className="p-5 space-y-6">

  {/* Header */}
  <div className="space-y-1">
    <div className="text-xs text-muted-foreground">
      Choose a search strategy and optional heuristic
    </div>
  </div>

  {/* Algorithm */}
  <div className="space-y-2">
    <div className="text-sm font-medium">Algorithm</div>

    <Select value={algorithm} onValueChange={(v: Algorithm) => setAlgorithm(v)}>
      <SelectTrigger className="w-full">
        <SelectValue placeholder="Select an algorithm (e.g. A*, BFS)" />
      </SelectTrigger>

      <SelectContent>
        <SelectItem value="bfs">BFS (Breadth-First Search)</SelectItem>
        <SelectItem value="dfs">DFS (Depth-First Search)</SelectItem>
        <SelectItem value="idfs">IDFS (Iterative Deepening)</SelectItem>
        <SelectItem value="astar">A* Search</SelectItem>
        <SelectItem value="greedy">Greedy</SelectItem>
      </SelectContent>
    </Select>
  </div>

  {/* Heuristic */}
  {showHeuristic && (
    <div className="space-y-2">
      <div className="text-sm font-medium">Heuristic</div>

      <Select value={heuristic} onValueChange={(v: Heuristic) => setHeuristic(v)}>
        <SelectTrigger className="w-full">
          <SelectValue placeholder="Choose heuristic (recommended: Manhattan)" />
        </SelectTrigger>

        <SelectContent>
          <SelectItem value="manhattan">
            Manhattan distance
          </SelectItem>
          <SelectItem value="sum">
            Misplaced tiles
          </SelectItem>
        </SelectContent>
      </Select>

      <div className="text-xs text-muted-foreground">
        Used only for informed search algorithms (A*, Greedy)
      </div>
    </div>
  )}

  {/* Button */}
  <Button
    className="w-full"
    disabled={loading}
    onClick={() =>
      onSolve(
        algorithm,
        showHeuristic ? heuristic : undefined
      )
    }
  >
    {loading ? "Solving..." : "Start solving"}
  </Button>

</Card>
  );
}