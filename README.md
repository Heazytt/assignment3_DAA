# 🌳 Minimum Spanning Tree (MST) Algorithms

## 🌟 MST Properties
- 🌲 MST for connected graph has exactly **V - 1** edges  
- 🌀 MST is **acyclic** (it's a tree)  
- 🔗 MST is **connected** (all vertices reachable)  
- 💰 Total cost of MST is **minimum**  
- 🌳 For disconnected graph we get a **forest** (multiple trees)

---

## ⚡ Prim's Algorithm

### 💡 Main Idea
A greedy algorithm that grows a tree from one starting vertex.  
At each step, it adds the minimum-weight edge connecting the tree to a vertex outside the tree.

### 🧭 How it works
1. 🚀 Start from any vertex  
2. 🌱 Add it to the tree  
3. 🔍 Find the minimum edge connecting the tree to an outside vertex  
4. 🔗 Add this edge and vertex to the tree  
5. 🔁 Repeat until all vertices are in the tree  

### 🧱 Data Structures
- 🧮 Priority Queue (Min-Heap) — store edges efficiently  
- 🪣 Set — track vertices already in the tree  

### ⏱️ Time Complexity
`O((V + E) log V)`

### 💾 Space Complexity
`O(V + E)`

### ✅ Best for
- 🌐 Dense graphs (many edges)  
- 📊 Graphs represented as adjacency matrices  

---

## 🧩 Kruskal's Algorithm

### 💡 Main Idea
A greedy algorithm that sorts all edges by weight and adds them one by one if they don't create a cycle.

### 🧭 How it works
1. 📑 Sort all edges by weight (ascending)  
2. 🧲 Take the minimum edge  
3. ❌ If it creates a cycle → skip  
4. ✅ If not → add to MST  
5. 🔁 Repeat until MST has `V−1` edges  

### 🧱 Data Structures
- 🔗 Disjoint Set Union (DSU) — detect cycles efficiently  
- 🧾 Sorted list of edges  

### ⚙️ DSU Optimizations
- 🪜 Path compression — flatten tree structure during find  
- 🧮 Union by rank — merge smaller tree into larger  

### ⏱️ Time Complexity
- Sorting edges: `O(E log E)`  
- DSU operations: `O(E α(V))`, where `α(V)` ≈ constant  
- Total: `O(E log E)`

### 💾 Space Complexity
`O(V + E)`

### ✅ Best for
- 🌿 Sparse graphs (few edges)  
- 📜 Graph as edge list  
- ⚡ When edges are already sorted  

---

## 📈 Complexity by Graph Type

### 🧱 Dense graph (E ≈ V²)
- Prim: `O(V² log V)`  
- Kruskal: `O(V² log V²)` = `O(V² log V)`  
➡️ Almost equal, Prim slightly better  

### 🌾 Sparse graph (E ≈ V)
- Prim: `O(V log V)`  
- Kruskal: `O(V log V)`  
➡️ Almost equal, Kruskal simpler  

---

## ⚔️ Comparison

| Aspect | 🟢 Prim | 🔵 Kruskal |
|:-------|:--------|:-----------|
| **Complexity** | O((V+E) log V) | O(E log E) |
| **Best for** | Dense graphs | Sparse graphs |
| **Data Structure** | Heap + Adjacency List | Sorted Edges + DSU |
| **Implementation** | Medium difficulty | Simpler |

and Theory and Practice in report.pdf
