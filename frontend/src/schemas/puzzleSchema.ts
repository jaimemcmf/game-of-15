import { z } from "zod";

export const PuzzleSchema = z.object({
  size: z.literal(4),
  tiles: z
    .array(z.number().int().min(0).max(15))
    .length(16)
    .refine(
      (arr) => new Set(arr).size === 16,
      "Tiles must be unique"
    ),
});

export type Puzzle = z.infer<typeof PuzzleSchema>;