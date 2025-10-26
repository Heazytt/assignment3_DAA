package org.example;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public final class Models {


    public static class InputRoot {
        @JsonProperty("graphs")
        public List<GraphIn> graphs;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class GraphIn {
        @JsonProperty("id") public int id;
        @JsonProperty("nodes") public List<String> nodes;
        @JsonProperty("edges") public List<EdgeIn> edges;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class EdgeIn {
        @JsonProperty("from") public String from;
        @JsonProperty("to") public String to;
        @JsonProperty("weight") public int weight;
    }


    public static class OutputRoot {
        @JsonProperty("results")
        public List<GraphOut> results;
        public OutputRoot(List<GraphOut> results) { this.results = results; }
    }

    public static class GraphStats {
        @JsonProperty("vertices") public int vertices;
        @JsonProperty("edges") public int edges;
        public GraphStats(int v, int e) { this.vertices = v; this.edges = e; }
    }

    public static class EdgeOut {
        @JsonProperty("from") public String from;
        @JsonProperty("to") public String to;
        @JsonProperty("weight") public int weight;
        public EdgeOut(String f, String t, int w) { from=f; to=t; weight=w; }
    }

    public static class AlgoResult {
        @JsonProperty("mst_edges") public List<EdgeOut> mstEdges;
        @JsonProperty("total_cost") public int totalCost;
        @JsonProperty("operations_count") public long operationsCount;
        @JsonProperty("execution_time_ms") public double executionTimeMs;

        public AlgoResult(List<EdgeOut> edges, int cost, long ops, double ms) {
            this.mstEdges = edges; this.totalCost = cost; this.operationsCount = ops; this.executionTimeMs = ms;
        }
    }

    public static class GraphOut {
        @JsonProperty("graph_id") public int graphId;
        @JsonProperty("input_stats") public GraphStats inputStats;
        @JsonProperty("prim") public AlgoResult prim;
        @JsonProperty("kruskal") public AlgoResult kruskal;

        public GraphOut(int id, GraphStats stats, AlgoResult p, AlgoResult k) {
            this.graphId = id; this.inputStats = stats; this.prim = p; this.kruskal = k;
        }
    }
}
