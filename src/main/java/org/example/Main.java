package org.example;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.Models.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {
        String inputPath  = args.length > 0 ? args[0] : "input_for_tests.json";
        String outputPath = args.length > 1 ? args[1] : "output.json";

        ObjectMapper om = new ObjectMapper();
        InputRoot root = om.readValue(new File(inputPath), new TypeReference<>() {});

        PrimMST prim = new PrimMST();
        KruskalMST kruskal = new KruskalMST();

        List<GraphOut> results = new ArrayList<>();

        for (GraphIn g : root.graphs) {
            var p = prim.run(g.nodes, g.edges);
            var k = kruskal.run(g.nodes, g.edges);

            GraphStats stats = new GraphStats(
                    g.nodes != null ? g.nodes.size() : 0,
                    g.edges != null ? g.edges.size() : 0
            );

            AlgoResult primRes = new AlgoResult(p.edges(), p.cost(), p.ops(), p.ms());
            AlgoResult krusRes = new AlgoResult(k.edges(), k.cost(), k.ops(), k.ms());

            results.add(new GraphOut(g.id, stats, primRes, krusRes));
        }

        OutputRoot out = new OutputRoot(results);
        File outFile = new File(outputPath);
        om.writerWithDefaultPrettyPrinter().writeValue(outFile, out);

        boolean ok = true;
        for (GraphOut go : results) {
            if (go.prim.totalCost != go.kruskal.totalCost) {
                ok = false;
                System.err.printf("Graph %d: Prim=%d vs Kruskal=%d%n",
                        go.graphId, go.prim.totalCost, go.kruskal.totalCost);
            }
        }
        System.out.println(ok ? "OK: Prim cost == Kruskal cost." : "WARNING: costs differ.");
        System.out.println("Written -> " + outFile.getAbsolutePath());


        System.out.println("\n=== METRICS FOR CHARTS ===");
        System.out.println("ID\tV\t\tE\t\t\tPrim_Cost\t\t\tKruskal_Cost\tPrim_Ops\tKruskal_Ops\tPrim_Time_ms\tKruskal_Time_ms");
        for (GraphOut go : results) {
            System.out.printf("%-4d %-8d %-8d %-23d %-13d %-10d %-12d %-13.4f %-15.4f%n",
                    go.graphId,
                    go.inputStats.vertices,
                    go.inputStats.edges,
                    go.prim.totalCost,
                    go.kruskal.totalCost,
                    go.prim.operationsCount,
                    go.kruskal.operationsCount,
                    go.prim.executionTimeMs,
                    go.kruskal.executionTimeMs
            );
        }

    }
}
