const API_BASE_URL = "http://localhost:8080";

export async function solvePuzzle(request: any) {
  const response = await fetch(
    `${API_BASE_URL}/solve`,
    {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(request),
    }
  );

  if (!response.ok) {
    throw new Error("Failed to solve puzzle");
  }

  return response.json();
}