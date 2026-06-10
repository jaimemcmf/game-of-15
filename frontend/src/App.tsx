import { useState } from "react";

import { PuzzleBoard } from "./components/PuzzleBoard";
import { Controls } from "./components/Controls";
import { Metrics } from "./components/Metrics";
import { SolutionMoves } from "./components/SolutionMoves";

import { compareAlgorithms, solvePuzzle } from "./services/api";
import type { SolveRequest } from "@/types/SolveRequest";

import { generateBoardFromGoal } from "./utils/puzzle";
import { animateSolution } from "./utils/animations";
import type { Result } from "./types/Result";
import type { Difficulty } from "./types/Difficulty";

export default function App() {
  const [state, setState] = useState(() => ({
    tiles: [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 0, 15],
  }));

  const [loading, setLoading] = useState(false);
  const [animating, setAnimating] = useState(false);

  const [metrics, setMetrics] = useState<Result[]>([]);
  const [displayedMoves, setDisplayedMoves] = useState<string[]>([]);

  const handleSolve = async (
    timeOutLimit: Number,
    algorithm: SolveRequest["searchAlgorithm"],
    heuristic?: SolveRequest["heuristic"]
  ) => {
    setLoading(true);

    try {
      const solveResult: Result = await solvePuzzle({
        initialState: state.tiles,
        searchAlgorithm: algorithm,
        heuristic,
        timeOutLimit
      });

      setMetrics([solveResult]);
      setDisplayedMoves(solveResult.moves);

      console.log("Solve result:", solveResult);
    } catch (e) {
      console.error(e);
    } finally {
      setLoading(false);
    }
  };

  const handleCompare = async (
    timeOutLimit: Number
  ) => {
    setLoading(true);

    try {
      const compareResponse = await compareAlgorithms({initialState: state.tiles, timeOutLimit: timeOutLimit});

      const results: Result[] = compareResponse.results;

      setMetrics(results);

      const validResults = results.filter(
        (r) => !r.timedOut && r.moves.length > 0,
      );

      if (validResults.length > 0) {
        const bestResult = validResults.reduce((best, current) =>
          current.depth < best.depth ? current : best,
        );

        setDisplayedMoves(bestResult.moves);
      }
    } catch (e) {
      console.error(e);
    } finally {
      setLoading(false);
    }
  };

  const handleRandomize = (difficulty: Difficulty) => {
    const numberOfMoves = {
      Easy: Math.floor(Math.random() * 6) + 3, // 3-8 moves
      Medium: Math.floor(Math.random() * 11) + 10, // 10-20 moves
      Hard: Math.floor(Math.random() * 21) + 25, // 25-45 moves
    };

    setState({
      tiles: generateBoardFromGoal(numberOfMoves[difficulty]),
    });

    setMetrics([]);
    setDisplayedMoves([]);
  };

  const handleReplay = async () => {
    if (!displayedMoves.length) return;

    setAnimating(true);

    try {
      await animateSolution(displayedMoves, setState, 250);
    } finally {
      setAnimating(false);
    }
  };

  return (
    <div className="min-h-screen bg-background flex justify-center p-6">
      <div className="w-full max-w-6xl grid grid-cols-1 md:grid-cols-[1fr_280px] gap-x-10 gap-y-2 items-center">
        {/* LEFT: Puzzle */}
        <div className="flex flex-col items-center w-full">
          <div className="w-[520px] h-[520px] flex items-center justify-center">
            <PuzzleBoard state={{ tiles: state.tiles }} setState={setState} />
          </div>

          {displayedMoves.length > 0 && (
            <SolutionMoves moves={displayedMoves} onReplay={handleReplay} />
          )}
        </div>

        {/* CONTROLS */}
        <div className="w-full relative right-15">
          <Controls
            onSolve={handleSolve}
            onCompare={handleCompare}
            onRandomize={handleRandomize}
            loading={loading || animating}
          />
        </div>

        {/* METRICS */}
        {metrics.length > 0 && (
          <div className="col-span-full">
            <Metrics data={metrics}/>
          </div>
        )}
      </div>
    </div>
  );
}
