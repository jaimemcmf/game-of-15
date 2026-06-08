import { useState } from "react";

import { PuzzleBoard } from "./components/PuzzleBoard";
import { Controls } from "./components/Controls";
import { Metrics } from "./components/Metrics";

import { compareAlgorithms, solvePuzzle } from "./services/api";
import type { SolveRequest } from "@/types/SolveRequest";

import { generateBoardFromGoal } from "./utils/puzzle";
import { animateSolution } from "./utils/animation";

type SolveResult = {
  algorithm: string;
  moves: string[];
  nodesExpanded: number;
  depth: number;
  timeMs: number;
};

type CompareResult = {
  algorithm: string;
  nodesExpanded: number;
  depth: number;
  timeMs: number;
}[];

export default function App() {
  const [state, setState] = useState(() => ({
    tiles: [1,2,3,4,5,6,7,8,9,10,11,12,13,14,0,15],
  }));

  const [loading, setLoading] = useState(false);
  const [animating, setAnimating] = useState(false);

  const [result, setResult] = useState<SolveResult | null>(null);
  const [compareResults, setCompareResults] = useState<CompareResult | null>(null);

  const handleSolve = async (
    algorithm: SolveRequest["searchAlgorithm"],
    heuristic?: SolveRequest["heuristic"],
  ) => {
    setLoading(true);

    try {
      const solveResult = await solvePuzzle({
        initialState: state.tiles,
        searchAlgorithm: algorithm,
        heuristic,
      });

      setCompareResults(null); // clear compare mode
      setResult(solveResult);

      setAnimating(true);
      try {
        await animateSolution(solveResult.moves, setState, 250);
      } finally {
        setAnimating(false);
      }
    } catch (e) {
      console.error(e);
    } finally {
      setLoading(false);
    }
  };

  const handleCompare = async () => {
    setLoading(true);

    try {
      const compareResults = await compareAlgorithms(state.tiles);

      setResult(null); // clear single solve
      setCompareResults(compareResults.results);
      try {
        await animateSolution(compareResults.results[0].moves, setState, 250);
      } finally {
        setAnimating(false);
      }
    } catch (e) {
      console.error(e);
    } finally {
      setLoading(false);
    }
  };

  const handleRandomize = () => {
    setState({
      tiles: generateBoardFromGoal(5),
    });

    setResult(null);
    setCompareResults(null);
  };

  return (
    <div className="min-h-screen bg-background flex justify-center p-6">
      <div className="w-full max-w-6xl grid grid-cols-1 md:grid-cols-[1fr_280px] gap-10 items-center">

        {/* LEFT: Puzzle */}
        <div className="flex items-center justify-center w-full">
          <div className="w-[520px] h-[520px] flex items-center justify-center">
            <PuzzleBoard state={{ tiles: state.tiles }} setState={setState} />
          </div>
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
        {(result || compareResults) && (
          <div className="col-span-full">
            <Metrics
              data={
                compareResults
                  ? compareResults
                  : result
                  ? [
                      {
                        algorithm: result.algorithm,
                        nodesExpanded: result.nodesExpanded,
                        depth: result.depth,
                        timeMs: result.timeMs,
                      },
                    ]
                  : []
              }
            />
          </div>
        )}
      </div>
    </div>
  );
}