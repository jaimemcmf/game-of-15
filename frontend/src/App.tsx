import { useState } from "react";

import { PuzzleBoard } from "./components/PuzzleBoard";
import { Controls } from "./components/Controls";

import { solvePuzzle } from "./services/api";
import type { SolveRequest } from "@/types/SolveRequest";

export default function App() {
  const [state, setState] = useState({
    tiles: [
      1, 2, 3, 4,
      5, 6, 7, 8,
      9, 10, 11, 12,
      13, 14, 0, 15,
    ],
  });

  const [loading, setLoading] = useState(false);

  const handleSolve = async (
    algorithm: SolveRequest["searchAlgorithm"],
    heuristic?: SolveRequest["heuristic"]
  ) => {
    setLoading(true);

    try {
      const result = await solvePuzzle({
        initialState: state.tiles,
        searchAlgorithm: algorithm,
        heuristic,
      });

      console.log("Solution:", result);

    } catch (e) {
      console.error(e);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="min-h-screen bg-background flex justify-center p-6">
  <div className="w-full max-w-6xl grid grid-cols-1 md:grid-cols-[1fr_280px] gap-10 items-center">

    {/* LEFT: Puzzle (true centered area) */}
    <div className="flex items-center justify-center w-full">
      <div className="w-[520px] h-[520px] flex items-center justify-center">
        <PuzzleBoard
          state={{ tiles: state.tiles }}
          setState={setState}
        />
      </div>
    </div>

    {/* RIGHT: Controls */}
    <div className="w-full">
      <div className="rounded-xl border bg-card shadow-sm p-4 space-y-4 w-full">

        <div className="text-lg font-semibold">
          Algorithm Settings
        </div>

        <Controls
          onSolve={handleSolve}
          loading={loading}
        />

      </div>
    </div>

  </div>
</div>
  );
}