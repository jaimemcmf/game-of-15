import type {SolveRequest} from "../types/SolveRequest";
import type {CompareRequest} from "../types/CompareRequest";

const BASE_URL = "http://localhost:8080";

export async function solvePuzzle(req: SolveRequest) {
  console.log("Solving puzzle with request:", req);
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

export async function compareAlgorithms(req: CompareRequest) {
  console.log("Comparing algorithms with request:", req);
  const res = await fetch(`${BASE_URL}/api/compare`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(req),
  });

  if (!res.ok) {
    throw new Error("Failed to compare algorithms");
  }

  return res.json();
}