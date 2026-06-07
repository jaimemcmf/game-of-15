export function isSolved(tiles: number[]): boolean {
  for (let i = 0; i < 15; i++) {
    if (tiles[i] !== i + 1) return false;
  }
  return tiles[15] === 0;
}

// src/utils/puzzle.ts

export function generateRandomBoard(): number[] {
  const tiles = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 0];

  for (let i = tiles.length - 1; i > 0; i--) {
    const j = Math.floor(Math.random() * (i + 1));
    [tiles[i], tiles[j]] = [tiles[j], tiles[i]];
  }

  return tiles;
}

export function isSolvable(board: number[]): boolean {
  const arr = board.filter(n => n !== 0);
  let inversions = 0;

  for (let i = 0; i < arr.length; i++) {
    for (let j = i + 1; j < arr.length; j++) {
      if (arr[i] > arr[j]) inversions++;
    }
  }

  const emptyRowFromBottom =
    4 - Math.floor(board.indexOf(0) / 4);

  if (4 % 2 === 1) {
    return inversions % 2 === 0;
  } else {
    return (emptyRowFromBottom % 2 === 0) !== (inversions % 2 === 0);
  }
}

export function generateSolvableBoard(): number[] {
  let board: number[];

  do {
    board = generateRandomBoard();
  } while (!isSolvable(board));

  return board;
}