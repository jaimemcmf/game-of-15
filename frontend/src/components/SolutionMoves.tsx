type SolutionMovesProps = {
  moves: string[];
};

const directionMap: Record<string, string> = {
  UP: "↑",
  DOWN: "↓",
  LEFT: "←",
  RIGHT: "→",
};

export function SolutionMoves({ moves }: SolutionMovesProps) {
  if (!moves.length) return null;

  return (
    <div className="w-full max-w-[520px] mt-6 rounded-xl bg-card p-4">
      <div className="flex items-center justify-between mb-3">
        <h3 className="font-semibold text-lg">Solution Path</h3>
        <span className="text-sm text-muted-foreground">
          {moves.length} moves
        </span>
      </div>

      <div className="flex flex-wrap gap-2 text-2xl">
        {moves.map((move, index) => (
          <span
            key={index}
            className="flex items-center justify-center w-10 h-10 rounded-md bg-secondary"
            title={`${index + 1}. ${move}`}
          >
            {directionMap[move.toUpperCase()] ?? "?"}
          </span>
        ))}
      </div>
    </div>
  );
}