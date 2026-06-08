import { useState } from "react";

import { Button } from "../../@/components/ui/button";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "../../@/components/ui/select";

import {
  Card,
  CardHeader,
  CardTitle,
  CardContent,
  CardFooter,
} from "../../@/components/ui/card";

import type { Algorithm, Heuristic } from "@/types/SolveRequest";

type Props = {
  onSolve: (algorithm: Algorithm, heuristic?: Heuristic) => void;
  onCompare: () => void;
  onRandomize: () => void;
  loading: boolean;
};

export function Controls({ onSolve, onCompare, onRandomize, loading }: Props) {
  const [algorithm, setAlgorithm] = useState<Algorithm>("astar");
  const [heuristic, setHeuristic] = useState<Heuristic>("manhattan");

  const showHeuristic = algorithm === "astar" || algorithm === "greedy";

  return (
    <Card className="border-none shadow-none">
      <CardHeader>
        <CardTitle>Controls</CardTitle>
      </CardHeader>

      <CardContent className="space-y-6">
        {/* Algorithm */}
        <div className="space-y-2">
          <div className="text-sm font-medium">Algorithm</div>

          <Select
            value={algorithm}
            onValueChange={(v: Algorithm) => setAlgorithm(v)}
          >
            <SelectTrigger className="w-full">
              <SelectValue placeholder="Select an algorithm" />
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

            <Select
              value={heuristic}
              onValueChange={(v: Heuristic) => setHeuristic(v)}
            >
              <SelectTrigger className="w-full">
                <SelectValue placeholder="Choose heuristic" />
              </SelectTrigger>

              <SelectContent>
                <SelectItem value="manhattan">Manhattan distance</SelectItem>
                <SelectItem value="sum">Misplaced tiles</SelectItem>
              </SelectContent>
            </Select>

            <p className="text-xs text-muted-foreground">
              Used only for informed search algorithms (A*, Greedy)
            </p>
          </div>
        )}
      </CardContent>

      <CardFooter className="flex flex-col gap-2">
        <Button
          className="w-full"
          disabled={loading}
          onClick={() =>
            onSolve(algorithm, showHeuristic ? heuristic : undefined)
          }
        >
          {loading ? "Solving..." : "Solve"}
        </Button>

        <Button
          className="w-full"
          disabled={loading}
          onClick={() => onCompare()}
        >
          {loading ? "Solving..." : "Compare All Algorithms"}
        </Button>

        <Button
          variant="outline"
          className="w-full"
          disabled={loading}
          onClick={onRandomize}
        >
          Randomize board
        </Button>
      </CardFooter>
    </Card>
  );
}
