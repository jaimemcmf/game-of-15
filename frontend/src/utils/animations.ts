import { moveBlank } from "./puzzle";

export function sleep(ms: number) {
  return new Promise((resolve) => setTimeout(resolve, ms));
}

export async function animateSolution(
  moves: string[],
  setState: React.Dispatch<any>,
  speed = 200,
) {
  for (const move of moves) {
    setState((prev: any) => ({
      tiles: moveBlank(prev.tiles, move),
    }));

    await new Promise((r) => setTimeout(r, speed));
  }
}

type Coord = { row: number; col: number };

export async function runSolveAnimation(
  totalTiles: number,
  gridSize: number,
  onStep: (active: Set<number>) => void,
) {
  const visited = new Set<string>();
  const active = new Set<number>();

  const queue: Coord[] = [{ row: 0, col: 0 }];

  const key = (r: number, c: number) => `${r},${c}`;

  const toIndex = (r: number, c: number) => r * gridSize + c;

  const dirs = [
    { r: 1, c: 0 },
    { r: -1, c: 0 },
    { r: 0, c: 1 },
    { r: 0, c: -1 },
  ];

  const delay = 35;

  while (queue.length) {
    const next: Coord[] = [];

    for (const { row, col } of queue) {
      const k = key(row, col);
      if (visited.has(k)) continue;

      visited.add(k);
      active.add(toIndex(row, col));

      onStep(new Set(active));

      for (const d of dirs) {
        const nr = row + d.r;
        const nc = col + d.c;

        if (
          nr >= 0 &&
          nc >= 0 &&
          nr < gridSize &&
          nc < gridSize &&
          !visited.has(key(nr, nc))
        ) {
          next.push({ row: nr, col: nc });
        }
      }

      await sleep(delay);
    }

    queue.length = 0;
    queue.push(...next);
  }

  // ensure all tiles are green at end
  onStep(new Set(Array.from({ length: totalTiles }, (_, i) => i)));
}
