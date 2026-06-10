import { useState } from "react";

import { Button } from "../../@/components/ui/button";
import { Input } from "../../@/components/ui/input";
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
import type { Difficulty } from "@/types/Difficulty";

type Props = {
  onSolve: (
    timeOutLimit: Number,
    algorithm: Algorithm,
    heuristic?: Heuristic,
  ) => void;
  onCompare: (timeOutLimit: Number) => void;
  onRandomize: (difficulty: Difficulty) => void;
  loading: boolean;
};

export function Controls({ onSolve, onCompare, onRandomize, loading }: Props) {
  const [algorithm, setAlgorithm] = useState<Algorithm | undefined>();
  const [heuristic, setHeuristic] = useState<Heuristic | undefined>();
  const [difficulty, setDifficulty] = useState<Difficulty>("Medium");
  const [timeOutLimit, setTimeOutLimit] = useState<number>(5);

  const showHeuristic =
    algorithm === "AStar" || algorithm === "Greedy" || algorithm === "IDAStar";

  return (
    <div className="space-y-4">
      {/* Solver Settings */}
      <Card className="border-none shadow-none">
        <CardHeader>
          <CardTitle>Solver Settings</CardTitle>
        </CardHeader>

        <CardContent className="space-y-6">
          {/* Algorithm */}
          <div className="space-y-2">
            <div className="text-sm font-medium">Algorithm</div>

            <Select
              value={algorithm}
              onValueChange={(v) => setAlgorithm(v as Algorithm)}
            >
              <SelectTrigger className="w-full">
                <SelectValue placeholder="Select an algorithm" />
              </SelectTrigger>

              <SelectContent>
                <SelectItem value="DFS">DFS (Depth-First Search)</SelectItem>
                <SelectItem value="IDFS">IDFS (Iterative Deepening)</SelectItem>
                <SelectItem value="BFS">BFS (Breadth-First Search)</SelectItem>
                <SelectItem value="Greedy">Greedy Best-First Search</SelectItem>
                <SelectItem value="AStar">A* Search</SelectItem>
                <SelectItem value="IDAStar">
                  Iterative Deepening A* Search
                </SelectItem>
              </SelectContent>
            </Select>
          </div>

          {/* Heuristic */}
          {showHeuristic && (
            <div className="space-y-2">
              <div className="text-sm font-medium">Heuristic</div>

              <Select
                value={heuristic}
                onValueChange={(v) => setHeuristic(v as Heuristic)}
              >
                <SelectTrigger className="w-full">
                  <SelectValue placeholder="Choose heuristic" />
                </SelectTrigger>

                <SelectContent>
                  <SelectItem value="Sum">Misplaced tiles</SelectItem>
                  <SelectItem value="Manhattan">Manhattan distance</SelectItem>
                  <SelectItem value="ManhattanLinearConflict">
                    Manhattan distance + Linear conflict
                  </SelectItem>
                </SelectContent>
              </Select>

              <p className="text-xs text-muted-foreground">
                Used only for informed search algorithms (A*, Greedy, IDA*)
              </p>
            </div>
          )}

          {/* Timeout */}
          <div className="space-y-2">
            <div className="text-sm font-medium">Timeout Limit (seconds)</div>

            <Input
              type="number"
              min={1}
              step={1}
              value={timeOutLimit}
              onChange={(e) => {
                const value = e.target.value;

                if (value === "") {
                  setTimeOutLimit(1);
                  return;
                }

                const num = Number(value);

                if (Number.isInteger(num) && num > 0) {
                  setTimeOutLimit(num);
                }
              }}
            />
          </div>
        </CardContent>

        <CardFooter className="flex flex-col gap-2">
          <Button
            className="w-full"
            disabled={loading || !algorithm || (showHeuristic && !heuristic)}
            onClick={() => {
              if (algorithm) {
                onSolve(
                  timeOutLimit,
                  algorithm,
                  showHeuristic ? heuristic : undefined,
                );
              }
            }}
          >
            {loading ? "Solving..." : "Solve"}
          </Button>

          <Button
            className="w-full"
            variant="secondary"
            disabled={loading}
            onClick={() => onCompare(timeOutLimit)}
          >
            {loading ? "Solving..." : "Compare All Algorithms"}
          </Button>
        </CardFooter>
      </Card>

      {/* Board Setup */}
      <Card className="border-none shadow-none">
        <CardHeader>
          <CardTitle>Board Setup</CardTitle>
        </CardHeader>

        <CardContent>
          <div className="space-y-2">
            <div className="text-sm font-medium">Difficulty</div>

            <Select
              value={difficulty}
              onValueChange={(v) => setDifficulty(v as Difficulty)}
            >
              <SelectTrigger className="w-full">
                <SelectValue />
              </SelectTrigger>

              <SelectContent>
                <SelectItem value="Easy">Easy</SelectItem>
                <SelectItem value="Medium">Medium</SelectItem>
                <SelectItem value="Hard">Hard</SelectItem>
              </SelectContent>
            </Select>
          </div>
        </CardContent>

        <CardFooter>
          <Button
            variant="outline"
            className="w-full"
            disabled={loading}
            onClick={() => onRandomize(difficulty)}
          >
            Shuffle
          </Button>
        </CardFooter>
      </Card>
    </div>
  );
}
