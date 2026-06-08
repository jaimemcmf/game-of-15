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
  console.log("Metrics data:", data);
  console.log("Is array:", Array.isArray(data));
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
              <TableCell className= "text-left">
                {row.algorithm}
              </TableCell>
              <TableCell className="text-left">{row.nodesExpanded}</TableCell>
              <TableCell className="text-left">{row.depth}</TableCell>
              <TableCell className="text-left">{row.timeMs}</TableCell>
            </TableRow>
          ))}
        </TableBody>
      </Table>
    </Card>
  );
}