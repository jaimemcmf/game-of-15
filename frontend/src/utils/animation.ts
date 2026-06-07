import { moveBlank } from "./puzzle";

export function sleep(ms: number) {
  return new Promise((resolve) => setTimeout(resolve, ms));
}

export async function animateSolution(
  moves: string[],
  setState: React.Dispatch<any>,
  speed = 200
) {
  for (const move of moves) {
    setState((prev: any) => ({
      tiles: moveBlank(prev.tiles, move),
    }));

    await new Promise(r => setTimeout(r, speed));
  }
}