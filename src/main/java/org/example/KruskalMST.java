package org.example;

import org.example.Models.EdgeIn;
import org.example.Models.EdgeOut;
import java.util.*;
import java.util.stream.Collectors;

public class KruskalMST {

    public record Result(List<EdgeOut> edges, int cost, long ops, double ms) {}

    public Result run(List<String> nodes, List<EdgeIn> edges) {
        long ops = 0;
        long t0 = System.nanoTime();

        List<String> safeNodes = (nodes != null) ? nodes : Collections.emptyList();
        List<EdgeIn> safeEdges = (edges != null) ? edges : Collections.emptyList();

        final int V = safeNodes.size();
        final int logE = Math.max(1, (int) Math.ceil(Math.log(Math.max(2, safeEdges.size())) / Math.log(2)));

        DSU dsu = new DSU();
        for (String v : safeNodes) dsu.makeSet(v);

        List<EdgeIn> sorted = safeEdges.stream()
                .sorted(Comparator.comparingInt(e -> e.weight))
                .collect(Collectors.toList());
        ops += (long) safeEdges.size() * logE;

        List<EdgeOut> mst = new ArrayList<>();
        int total = 0;

        for (EdgeIn e : sorted) {
            ops += logE;
            if (!dsu.hasNode(e.from) || !dsu.hasNode(e.to)) continue;
            if (dsu.union(e.from, e.to)) {
                mst.add(new EdgeOut(e.from, e.to, e.weight));
                total += e.weight;
                if (mst.size() == Math.max(0, safeNodes.size() - 1)) break;
            }
        }

        ops += dsu.ops;
        double ms = (System.nanoTime() - t0) / 1_000_000.0;
        return new Result(mst, total, ops, ms);
    }
}
