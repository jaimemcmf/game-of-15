import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "../../@/components/ui/table";
import { Card } from "../../@/components/ui/card";
import { Badge } from "../../@/components/ui/badge";

import type { Result } from "../types/Result";

type Props = {
  data: Result[];
};

const formatMetric = (row: Result) => {
  if (row.timedOut) {
    return {
      depth: "—",
      time: "—",
    };
  }

  return {
    nodes: row.nodesExpanded,
    depth: row.depth,
    time: row.timeMs,
  };
};

const getStatusBadge = (timedOut: boolean) =>
  timedOut
    ? {
        label: "TIMEOUT",
        className: "bg-red-500 text-white hover:bg-red-500",
      }
    : {
        label: "SOLVED",
        className: "bg-green-500 hover:bg-green-500",
      };

export function Metrics({ data }: Props) {
  return (
    <Card className="p-6">
      <div className="text-sm font-semibold">Algorithm Performance</div>

      <Table>
        <TableHeader>
          <TableRow>
            <TableHead>Algorithm</TableHead>
            <TableHead>Heuristic</TableHead>
            <TableHead>Expanded Nodes</TableHead>
            <TableHead>Depth</TableHead>
            <TableHead>Time (ms)</TableHead>
            <TableHead>Status</TableHead>
          </TableRow>
        </TableHeader>

        <TableBody>
          {data.map((row) => {
            const f = formatMetric(row);
            const status = getStatusBadge(row.timedOut);

            return (
              <TableRow
                className="text-left"
                key={`${row.algorithm}-${row.heuristic ?? "none"}`}
              >
                <TableCell>{row.algorithm}</TableCell>

                <TableCell>{row.heuristic ?? "—"}</TableCell>

                <TableCell>{row.nodesExpanded}</TableCell>

                <TableCell>{f.depth}</TableCell>

                <TableCell>{f.time}</TableCell>

                <TableCell>
                  <Badge className={status.className}>{status.label}</Badge>
                </TableCell>
              </TableRow>
            );
          })}
        </TableBody>
      </Table>
    </Card>
  );
}
