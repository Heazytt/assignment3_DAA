package org.example;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.Models.*;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;


public class CSVGenerator {
    public static void main(String[] args) throws Exception {
        String inputJson = args.length > 0 ? args[0] : "assign_3_output.json";
        String outputCsv = args.length > 1 ? args[1] : "results.csv";

        ObjectMapper om = new ObjectMapper();
        OutputRoot root = om.readValue(new File(inputJson), new TypeReference<>() {});

        try (PrintWriter pw = new PrintWriter(new FileWriter(outputCsv))) {

            pw.println("Graph ID,Vertices,Edges,Prim Cost,Kruskal Cost,Prim Ops,Kruskal Ops,Prim Time (ms),Kruskal Time (ms),Winner (Time)");

            for (GraphOut g : root.results) {
                pw.printf("%d,%d,%d,%d,%d,%d,%d,%.4f,%.4f,%s%n",
                        g.graphId,
                        g.inputStats.vertices,
                        g.inputStats.edges,
                        g.prim.totalCost,
                        g.kruskal.totalCost,
                        g.prim.operationsCount,
                        g.kruskal.operationsCount,
                        g.prim.executionTimeMs,
                        g.kruskal.executionTimeMs,
                        (g.prim.executionTimeMs < g.kruskal.executionTimeMs) ? "Prim" :
                        (g.kruskal.executionTimeMs < g.prim.executionTimeMs) ? "Kruskal" : "Tie"
                );
            }
        }

        System.out.println("CSV generated: " + new File(outputCsv).getAbsolutePath());
    }
}

