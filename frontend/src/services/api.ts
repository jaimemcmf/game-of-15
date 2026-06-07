import type {SolveRequest} from "../types/SolveRequest";

const BASE_URL = "http://localhost:8080";

export async function solvePuzzle(req: SolveRequest) {
  const res = await fetch(`${BASE_URL}/api/solver`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(req),
  });

  if (!res.ok) {
    throw new Error("Failed to solve puzzle");
  }

  return res.json();
}