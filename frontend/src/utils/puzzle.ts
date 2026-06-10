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

export function generateBoardFromGoal(moves = 5): number[] {
  let board = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 0];

  let lastMove: string | null = null;

  for (let i = 0; i < moves; i++) {
    const possibleMoves = ["up", "down", "left", "right"];

    // avoid undoing the previous move
    const opposite: Record<string, string> = {
      up: "down",
      down: "up",
      left: "right",
      right: "left",
    };

    const filtered = possibleMoves.filter(
      (m) => m !== opposite[lastMove ?? ""],
    );

    const move = filtered[Math.floor(Math.random() * filtered.length)];

    const next = moveBlank(board, move);

    if (next !== board) {
      board = next;
      lastMove = move;
    } else {
      i--;
    }
  }

  return board;
}

export function isSolvable(board: number[]): boolean {
  const arr = board.filter((n) => n !== 0);
  let inversions = 0;

  for (let i = 0; i < arr.length; i++) {
    for (let j = i + 1; j < arr.length; j++) {
      if (arr[i] > arr[j]) inversions++;
    }
  }

  const emptyRowFromBottom = 4 - Math.floor(board.indexOf(0) / 4);

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

export function moveBlank(board: number[], direction: string): number[] {
  const newBoard = [...board];
  const index = newBoard.indexOf(0);

  const row = Math.floor(index / 4);
  const col = index % 4;

  let swapIndex = -1;

  direction = direction.toLowerCase();

  if (direction === "up" && row > 0) {
    swapIndex = index - 4;
  }

  if (direction === "down" && row < 3) {
    swapIndex = index + 4;
  }

  if (direction === "left" && col > 0) {
    swapIndex = index - 1;
  }

  if (direction === "right" && col < 3) {
    swapIndex = index + 1;
  }

  // invalid move → return same board
  if (swapIndex === -1) return board;

  [newBoard[index], newBoard[swapIndex]] = [
    newBoard[swapIndex],
    newBoard[index],
  ];

  return newBoard;
}
