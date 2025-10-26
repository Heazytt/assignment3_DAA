package org.example;

import org.example.Models.EdgeIn;
import org.example.Models.EdgeOut;

import java.util.*;


public class PrimMST {

    public record Result(List<EdgeOut> edges, int cost, long ops, double ms) {}

    public Result run(List<String> nodes, List<EdgeIn> edges) {
        long ops = 0;
        long t0 = System.nanoTime();

        if (nodes == null || nodes.isEmpty()) {
            return new Result(List.of(), 0, 0, 0);
        }
        List<EdgeIn> safeEdges = (edges != null) ? edges : Collections.emptyList();


        Map<String, List<EdgeIn>> adj = new HashMap<>();
        for (String v : nodes) adj.put(v, new ArrayList<>());
        for (EdgeIn e : safeEdges) {
            adj.computeIfAbsent(e.from, k -> new ArrayList<>()).add(e);
            EdgeIn back = new EdgeIn();
            back.from = e.to; back.to = e.from; back.weight = e.weight;
            adj.computeIfAbsent(e.to, k -> new ArrayList<>()).add(back);
        }

        Set<String> inTree = new HashSet<>();
        PriorityQueue<EdgeIn> pq = new PriorityQueue<>(Comparator.comparingInt(x -> x.weight));

        List<EdgeOut> mst = new ArrayList<>();
        int total = 0;


        while (inTree.size() < nodes.size()) {

            if (pq.isEmpty()) {

                String start = null;
                for (String v : nodes) {
                    if (!inTree.contains(v)) { start = v; break; }
                }
                if (start == null) break; // на всякий случай
                inTree.add(start);
                List<EdgeIn> startEdges = adj.getOrDefault(start, List.of());
                pq.addAll(startEdges);
                ops += startEdges.size();

            }

            while (!pq.isEmpty()) {
                EdgeIn e = pq.poll();
                ops++;
                if (inTree.contains(e.to)) continue;

                mst.add(new EdgeOut(e.from, e.to, e.weight));
                total += e.weight;
                inTree.add(e.to);

                for (EdgeIn ne : adj.getOrDefault(e.to, List.of())) {
                    ops++;
                    if (!inTree.contains(ne.to)) {
                        pq.offer(ne);
                    }
                }


                if (inTree.size() >= nodes.size()) break;
            }
        }

        double ms = (System.nanoTime() - t0) / 1_000_000.0;
        return new Result(mst, total, ops, ms);
    }
}
