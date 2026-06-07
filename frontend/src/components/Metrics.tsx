import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "../../@/components/ui/table";
import { Card } from "../../@/components/ui/card";

type Metric = {
  algorithm: string;
  nodesExpanded: number;
  depth: number;
  timeMs: number;
};

type Props = {
  data: Metric[];
};

export function Metrics({ data }: Props) {
  return (
    <Card className="p-4">
      <div className="text-sm font-semibold mb-3">
        Algorithm Performance
      </div>

      <Table>
        <TableHeader>
          <TableRow>
            <TableHead>Algorithm</TableHead>
            <TableHead>Nodes</TableHead>
            <TableHead>Depth</TableHead>
            <TableHead>Time (ms)</TableHead>
          </TableRow>
        </TableHeader>

        <TableBody>
          {data.map((row) => (
            <TableRow key={row.algorithm}>
              <TableCell className="font-medium">
                {row.algorithm}
              </TableCell>
              <TableCell>{row.nodesExpanded}</TableCell>
              <TableCell>{row.depth}</TableCell>
              <TableCell>{row.timeMs}</TableCell>
            </TableRow>
          ))}
        </TableBody>
      </Table>
    </Card>
  );
}