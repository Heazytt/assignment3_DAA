package org.example;

import java.util.HashMap;
import java.util.Map;

public class DSU {
    private final Map<String, String> parent = new HashMap<>();
    private final Map<String, Integer> rank = new HashMap<>();
    public long ops = 0;

    public void makeSet(String x) {
        parent.putIfAbsent(x, x);
        rank.putIfAbsent(x, 0);
    }

    public boolean hasNode(String x) {
        return parent.containsKey(x);
    }

    public String find(String x) {
        ops++;
        if (!parent.get(x).equals(x)) {
            parent.put(x, find(parent.get(x)));
        }
        return parent.get(x);
    }

    public boolean union(String a, String b) {
        String pa = find(a), pb = find(b);
        if (pa.equals(pb)) return false;
        int ra = rank.get(pa), rb = rank.get(pb);
        ops++;
        if (ra < rb) {
            parent.put(pa, pb);
        } else if (ra > rb) {
            parent.put(pb, pa);
        } else {
            parent.put(pb, pa);
            rank.put(pa, ra + 1);
        }
        return true;
    }
}
