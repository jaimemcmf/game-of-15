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

type Metric = {
  algorithm: string;
  heuristic: string;
  nodesExpanded: number;
  depth: number;
  timeMs: number;
  timedOut: boolean;
};

type Props = {
  data: Metric[];
};

const formatMetric = (row: Metric) => {
  if (row.timedOut) {
    return {
      nodes: "—",
      depth: "—",
      time: `>${row.timeMs}`,
    };
  }

  return {
    nodes: row.nodesExpanded,
    depth: row.depth,
    time: `${row.timeMs}`,
  };
};

const getStatusBadge = (timeout: boolean) => {
  if (timeout) {
    return {
      label: "TIMEOUT",
      className: "bg-red-500 text-white hover:bg-red-500",
    };
  }

  return {
    label: "SOLVED",
    className: "bg-green-500 hover:bg-green-500",
  };
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

            return (
              <TableRow key={row.algorithm + row.heuristic}>
                <TableCell className="text-left">{row.algorithm}</TableCell>

                <TableCell className="text-left">
                  {row.heuristic || "—"}
                </TableCell>

                <TableCell className="text-left">{f.nodes}</TableCell>

                <TableCell className="text-left">{f.depth}</TableCell>

                <TableCell className="text-left">{f.time}</TableCell>

                <TableCell className="text-left">
                  {(() => {
                    const status = getStatusBadge(row.timedOut);

                    return (
                      <Badge className={status.className}>{status.label}</Badge>
                    );
                  })()}
                </TableCell>
              </TableRow>
            );
          })}
        </TableBody>
      </Table>
    </Card>
  );
}
