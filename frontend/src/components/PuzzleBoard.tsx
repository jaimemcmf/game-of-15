import { useEffect, useRef, useState } from "react";
import { Card } from "../../@/components/ui/card";
import type { PuzzleState } from "../types/Puzzle";
import { isSolved } from "@/utils/puzzle";
import { sleep, runSolveAnimation } from "@/utils/animations";

type Props = {
  state: PuzzleState;
  setState: (state: PuzzleState) => void;
};

export function PuzzleBoard({ state, setState }: Props) {
  const { tiles } = state;

  const [animating, setAnimating] = useState(false);
  const [greenTiles, setGreenTiles] = useState<Set<number>>(new Set());

  const solvedRef = useRef(false);

  const gridSize = 4;

  const getRowCol = (index: number) => ({
    row: Math.floor(index / gridSize),
    col: index % gridSize,
  });

  const isAdjacent = (i1: number, i2: number) => {
    const a = getRowCol(i1);
    const b = getRowCol(i2);

    return Math.abs(a.row - b.row) + Math.abs(a.col - b.col) === 1;
  };

  const handleClick = async (index: number) => {
    if (animating) return;

    const zeroIndex = tiles.indexOf(0);
    if (!isAdjacent(index, zeroIndex)) return;

    setAnimating(true);

    const newTiles = [...tiles];

    [newTiles[index], newTiles[zeroIndex]] = [
      newTiles[zeroIndex],
      newTiles[index],
    ];

    setState({ ...state, tiles: newTiles });
    await sleep(120);

    setAnimating(false);
  };

  const solved = isSolved(tiles);

  useEffect(() => {
    if (solved && !solvedRef.current) {
      solvedRef.current = true;

      runSolveAnimation(tiles.length, gridSize, setGreenTiles);
    }

    if (!solved) {
      solvedRef.current = false;
      setGreenTiles(new Set());
    }
  }, [solved, tiles.length]);

  return (
    <div className="space-y-3">
      <Card className="p-6 w-max mx-auto">
        <div className="grid grid-cols-4 gap-3">
          {tiles.map((value, idx) => (
            <div
              key={idx}
              onClick={() => handleClick(idx)}
              className={`
                w-20 h-20 md:w-24 md:h-24
                flex items-center justify-center
                text-xl md:text-2xl font-bold
                rounded-md
                border
                cursor-pointer
                transition-all duration-200

                ${
                  value === 0
                    ? "bg-transparent border-dashed"
                    : greenTiles.has(idx)
                    ? "bg-green-500 text-white"
                    : "bg-muted hover:bg-yellow-100"
                }
              `}
            >
              {value === 0 ? "" : value}
            </div>
          ))}
        </div>
      </Card>
    </div>
  );
}