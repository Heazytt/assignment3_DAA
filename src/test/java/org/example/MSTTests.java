package org.example;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.Models.*;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class MSTTests {

    private InputRoot readInput(String resourcePath) throws Exception {
        ObjectMapper om = new ObjectMapper();
        try (InputStream is = getClass().getClassLoader().getResourceAsStream(resourcePath)) {
            assertNotNull(is, "Resource not found: " + resourcePath);
            return om.readValue(is, new TypeReference<>() {});
        }
    }


    private boolean hasNoCycles(List<String> nodes, List<EdgeOut> mstEdges) {
        DSU dsu = new DSU();
        for (String v : nodes) dsu.makeSet(v);
        for (EdgeOut e : mstEdges) {
            if (!dsu.union(e.from, e.to)) {
                return false; // найден цикл
            }
        }
        return true;
    }


    private boolean isConnected(List<String> nodes, List<EdgeOut> mstEdges) {
        if (nodes.isEmpty()) return true;
        if (mstEdges.isEmpty() && nodes.size() > 1) return false;


        Set<String> visited = new HashSet<>();
        Set<String> toVisit = new HashSet<>();
        toVisit.add(nodes.get(0));

        while (!toVisit.isEmpty()) {
            String v = toVisit.iterator().next();
            toVisit.remove(v);
            visited.add(v);

            for (EdgeOut e : mstEdges) {
                if (e.from.equals(v) && !visited.contains(e.to)) {
                    toVisit.add(e.to);
                }
                if (e.to.equals(v) && !visited.contains(e.from)) {
                    toVisit.add(e.from);
                }
            }
        }

        return visited.size() == nodes.size();
    }

    private void assertMSTProperties(GraphIn g, PrimMST.Result p, KruskalMST.Result k, boolean shouldBeConnected) {

        assertEquals(p.cost(), k.cost(), "Prim vs Kruskal total cost must be equal");


        assertTrue(p.ms() >= 0, "Prim execution time must be non-negative");
        assertTrue(k.ms() >= 0, "Kruskal execution time must be non-negative");


        assertTrue(p.ops() >= 0, "Prim operations count must be non-negative");
        assertTrue(k.ops() >= 0, "Kruskal operations count must be non-negative");

        if (shouldBeConnected) {

            assertEquals(g.nodes.size() - 1, p.edges().size(), "Prim MST edges count must be V-1");
            assertEquals(g.nodes.size() - 1, k.edges().size(), "Kruskal MST edges count must be V-1");


            assertTrue(hasNoCycles(g.nodes, p.edges()), "Prim MST must be acyclic");
            assertTrue(hasNoCycles(g.nodes, k.edges()), "Kruskal MST must be acyclic");


            assertTrue(isConnected(g.nodes, p.edges()), "Prim MST must connect all vertices");
            assertTrue(isConnected(g.nodes, k.edges()), "Kruskal MST must connect all vertices");
        } else {

            assertTrue(p.edges().size() < g.nodes.size() - 1, "Disconnected graph: Prim MST edges < V-1");
            assertTrue(k.edges().size() < g.nodes.size() - 1, "Disconnected graph: Kruskal MST edges < V-1");
        }
    }

    @Test
    void smallGraphLine() throws Exception {
        InputRoot root = readInput("inputs/line.json");
        PrimMST prim = new PrimMST();
        KruskalMST kruskal = new KruskalMST();

        GraphIn g = root.graphs.get(0);
        var p = prim.run(g.nodes, g.edges);
        var k = kruskal.run(g.nodes, g.edges);

        assertMSTProperties(g, p, k, true);
    }

    @Test
    void smallGraphTriangle() throws Exception {
        InputRoot root = readInput("inputs/triangle.json");
        PrimMST prim = new PrimMST();
        KruskalMST kruskal = new KruskalMST();

        GraphIn g = root.graphs.get(0);
        var p = prim.run(g.nodes, g.edges);
        var k = kruskal.run(g.nodes, g.edges);

        assertMSTProperties(g, p, k, true);
    }

    @Test
    void smallGraphComplete() throws Exception {
        InputRoot root = readInput("inputs/small_complete.json");
        PrimMST prim = new PrimMST();
        KruskalMST kruskal = new KruskalMST();

        GraphIn g = root.graphs.get(0);
        var p = prim.run(g.nodes, g.edges);
        var k = kruskal.run(g.nodes, g.edges);

        assertMSTProperties(g, p, k, true);
    }

    @Test
    void smallGraphDense() throws Exception {
        InputRoot root = readInput("inputs/small_dense.json");
        PrimMST prim = new PrimMST();
        KruskalMST kruskal = new KruskalMST();

        GraphIn g = root.graphs.get(0);
        var p = prim.run(g.nodes, g.edges);
        var k = kruskal.run(g.nodes, g.edges);

        assertMSTProperties(g, p, k, true);
    }

    @Test
    void mediumGraph() throws Exception {
        InputRoot root = readInput("inputs/medium_graph.json");
        PrimMST prim = new PrimMST();
        KruskalMST kruskal = new KruskalMST();

        GraphIn g = root.graphs.get(0);
        var p = prim.run(g.nodes, g.edges);
        var k = kruskal.run(g.nodes, g.edges);

        assertMSTProperties(g, p, k, true);
    }

    @Test
    void largeGraph() throws Exception {
        InputRoot root = readInput("inputs/large_graph.json");
        PrimMST prim = new PrimMST();
        KruskalMST kruskal = new KruskalMST();

        GraphIn g = root.graphs.get(0);
        var p = prim.run(g.nodes, g.edges);
        var k = kruskal.run(g.nodes, g.edges);

        assertMSTProperties(g, p, k, true);
    }

    @Test
    void disconnectedGraph() throws Exception {
        InputRoot root = readInput("inputs/disconnected.json");
        PrimMST prim = new PrimMST();
        KruskalMST kruskal = new KruskalMST();

        GraphIn g = root.graphs.get(0);
        var p = prim.run(g.nodes, g.edges);
        var k = kruskal.run(g.nodes, g.edges);

        assertMSTProperties(g, p, k, false);
    }

    @Test
    void reproducibilityTest() throws Exception {

        InputRoot root = readInput("inputs/triangle.json");
        PrimMST prim = new PrimMST();
        KruskalMST kruskal = new KruskalMST();

        GraphIn g = root.graphs.get(0);

        var p1 = prim.run(g.nodes, g.edges);
        var p2 = prim.run(g.nodes, g.edges);
        var k1 = kruskal.run(g.nodes, g.edges);
        var k2 = kruskal.run(g.nodes, g.edges);

        assertEquals(p1.cost(), p2.cost(), "Prim results must be reproducible");
        assertEquals(k1.cost(), k2.cost(), "Kruskal results must be reproducible");
    }
}
