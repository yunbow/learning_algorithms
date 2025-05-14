// java
// グラフの最小全域木: クラスカル法 (Kruskal)

import java.util.*;

    
/**
 * Union-Find（素集合データ構造）を実装するクラス
 */
class DSU {
    private Map<String, String> parent;
    private Map<String, Integer> rank;
    
    /**
     * コンストラクタ
     * @param vertices 頂点のリスト
     */
    public DSU(List<String> vertices) {
        parent = new HashMap<>();
        rank = new HashMap<>();
        
        // 各頂点の親を自分自身に初期化
        for (String v : vertices) {
            parent.put(v, v);
            rank.put(v, 0);
        }
    }
    
    /**
     * 頂点iが属する集合の代表元（根）を見つける
     * @param i 頂点
     * @return 根の頂点
     */
    public String find(String i) {
        if (parent.get(i).equals(i)) {
            return i;
        }
        // パス圧縮の実装
        parent.put(i, find(parent.get(i)));
        return parent.get(i);
    }
    
    /**
     * 頂点iと頂点jを含む二つの集合を結合する
     * @param i 頂点1
     * @param j 頂点2
     * @return 結合が行われた場合はtrue、すでに同じ集合に属している場合はfalse
     */
    public boolean union(String i, String j) {
        String rootI = find(i);
        String rootJ = find(j);
        
        // 根が同じ場合は何もしない
        if (rootI.equals(rootJ)) {
            return false;
        }
        
        // ランクが小さい方の木をランクが大きい方の木の根に付ける
        if (rank.get(rootI) < rank.get(rootJ)) {
            parent.put(rootI, rootJ);
        } else if (rank.get(rootI) > rank.get(rootJ)) {
            parent.put(rootJ, rootI);
        } else {
            // ランクが同じ場合はどちらかを親とし、そのランクを増やす
            parent.put(rootJ, rootI);
            rank.put(rootI, rank.get(rootI) + 1);
        }
        return true;
    }
}

/**
 * グラフデータを管理するクラス
 */
class GraphData {
    // 隣接リストによるグラフ表現
    private Map<String, List<Pair<String, Integer>>> data;
    
    /**
     * コンストラクタ
     */
    public GraphData() {
        data = new HashMap<>();
    }
    
    /**
     * グラフの内部データを取得
     * @return グラフデータ
     */
    public Map<String, List<Pair<String, Integer>>> get() {
        return data;
    }
    
    /**
     * グラフの全頂点をリストとして取得
     * @return 頂点リスト
     */
    public List<String> getVertices() {
        return new ArrayList<>(data.keySet());
    }
    
    /**
     * グラフの全辺をリストとして取得
     * @return 辺のリスト（始点、終点、重み）のタプル
     */
    public List<Triple<String, String, Integer>> getEdges() {
        Set<Triple<String, String, Integer>> edges = new HashSet<>();
        
        for (String vertex : data.keySet()) {
            for (Pair<String, Integer> neighbor : data.get(vertex)) {
                String neighborVertex = neighbor.first;
                int weight = neighbor.second;
                
                // 辺を正規化（小さい方の頂点を最初にする）
                String[] sortedVertices = new String[]{vertex, neighborVertex};
                Arrays.sort(sortedVertices);
                
                edges.add(new Triple<>(sortedVertices[0], sortedVertices[1], weight));
            }
        }
        
        return new ArrayList<>(edges);
    }
        
    /**
     * 新しい頂点をグラフに追加
     * @param vertex 追加する頂点
     * @return 成功した場合はtrue
     */
    public boolean addVertex(String vertex) {
        if (!data.containsKey(vertex)) {
            data.put(vertex, new ArrayList<>());
        }
        return true;
    }
    
    /**
     * 両頂点間に辺を追加
     * @param vertex1 頂点1
     * @param vertex2 頂点2
     * @param weight 辺の重み
     * @return 成功した場合はtrue
     */
    public boolean addEdge(String vertex1, String vertex2, int weight) {
        // 頂点が存在しない場合は追加
        if (!data.containsKey(vertex1)) {
            addVertex(vertex1);
        }
        if (!data.containsKey(vertex2)) {
            addVertex(vertex2);
        }
        
        // vertex1 -> vertex2 の辺を追加（重み付き）
        boolean edgeExistsV1V2 = false;
        for (int i = 0; i < data.get(vertex1).size(); i++) {
            Pair<String, Integer> pair = data.get(vertex1).get(i);
            if (pair.first.equals(vertex2)) {
                // 既に存在する場合は重みを更新
                data.get(vertex1).set(i, new Pair<>(vertex2, weight));
                edgeExistsV1V2 = true;
                break;
            }
        }
        if (!edgeExistsV1V2) {
            data.get(vertex1).add(new Pair<>(vertex2, weight));
        }
        
        // vertex2 -> vertex1 の辺を追加（重み付き）
        boolean edgeExistsV2V1 = false;
        for (int i = 0; i < data.get(vertex2).size(); i++) {
            Pair<String, Integer> pair = data.get(vertex2).get(i);
            if (pair.first.equals(vertex1)) {
                // 既に存在する場合は重みを更新
                data.get(vertex2).set(i, new Pair<>(vertex1, weight));
                edgeExistsV2V1 = true;
                break;
            }
        }
        if (!edgeExistsV2V1) {
            data.get(vertex2).add(new Pair<>(vertex1, weight));
        }
        
        return true;
    }
        
    /**
     * グラフを空にする
     * @return 成功した場合はtrue
     */
    public boolean clear() {
        data.clear();
        return true;
    }
    
    /**
     * 最小全域木を取得
     * @return 最小全域木の辺のリスト
     */
    public List<Triple<String, String, Integer>> getMst() {
        // 1. 全ての辺を取得し、重みでソート
        List<Triple<String, String, Integer>> edges = getEdges();
        // 重みでソート
        edges.sort(Comparator.comparing(Triple::getThird));
        
        // 2. Union-Findデータ構造を初期化
        List<String> vertices = getVertices();
        DSU dsu = new DSU(vertices);
        
        // 3. MSTを構築
        List<Triple<String, String, Integer>> mstEdges = new ArrayList<>();
        int edgesCount = 0;
        
        // ソートされた辺を順番に調べる
        for (Triple<String, String, Integer> edge : edges) {
            String u = edge.first;
            String v = edge.second;
            int weight = edge.third;
            
            // 辺の両端点が属する集合の代表元を見つける
            String rootU = dsu.find(u);
            String rootV = dsu.find(v);
            
            // 両端点が異なる集合に属する場合、その辺をMSTに追加
            if (!rootU.equals(rootV)) {
                mstEdges.add(new Triple<>(u, v, weight));
                dsu.union(u, v);
                edgesCount++;
                
                // 頂点数から1を引いた数の辺がMSTに追加されたら終了
                if (edgesCount == vertices.size() - 1) {
                    break;
                }
            }
        }
        
        return mstEdges;
    }
}

/**
 * ペア（2つの値の組）を表すクラス
 */
static class Pair<A, B> {
    public final A first;
    public final B second;
    
    public Pair(A first, B second) {
        this.first = first;
        this.second = second;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pair<?, ?> pair = (Pair<?, ?>) o;
        return Objects.equals(first, pair.first) &&
                Objects.equals(second, pair.second);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(first, second);
    }
}

/**
 * トリプル（3つの値の組）を表すクラス
 */
static class Triple<A, B, C> {
    public final A first;
    public final B second;
    public final C third;
    
    public Triple(A first, B second, C third) {
        this.first = first;
        this.second = second;
        this.third = third;
    }
    
    public C getThird() {
        return third;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Triple<?, ?, ?> triple = (Triple<?, ?, ?>) o;
        return Objects.equals(first, triple.first) &&
                Objects.equals(second, triple.second) &&
                Objects.equals(third, triple.third);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(first, second, third);
    }
}

public class KruskalDemo {
    public static void main(String[] args) {
        System.out.println("Kruskal TEST -----> start");
        GraphData graphData = new GraphData();
        
        // テストケース1
        graphData.clear();
        Object[][] inputList1 = {
            {"A", "B", 4}, {"B", "C", 3}, {"B", "D", 2}, 
            {"D", "A", 1}, {"A", "C", 2}, {"B", "D", 2}
        };
        
        for (Object[] input : inputList1) {
            graphData.addEdge((String)input[0], (String)input[1], (Integer)input[2]);
        }
        
        System.out.println("\nグラフの頂点: " + graphData.getVertices());
        System.out.println("グラフの辺 (重み付き): " + graphData.getEdges());
        
        List<Triple<String, String, Integer>> outputMst1 = graphData.getMst();
        for (Triple<String, String, Integer> edge : outputMst1) {
            System.out.println("Edge: " + edge.first + " - " + edge.second + ", Weight: " + edge.third);
        }
        
        int totalWeight1 = outputMst1.stream().mapToInt(Triple::getThird).sum();
        System.out.println("最小全域木の合計重み: " + totalWeight1);
        
        // テストケース2
        graphData.clear();
        Object[][] inputList2 = {
            {"A", "B", 4}, {"C", "D", 4}, {"E", "F", 1}, {"F", "G", 1}
        };
        
        for (Object[] input : inputList2) {
            graphData.addEdge((String)input[0], (String)input[1], (Integer)input[2]);
        }
        
        System.out.println("\nグラフの頂点: " + graphData.getVertices());
        System.out.println("グラフの辺 (重み付き): " + graphData.getEdges());
        
        List<Triple<String, String, Integer>> outputMst2 = graphData.getMst();
        for (Triple<String, String, Integer> edge : outputMst2) {
            System.out.println("Edge: " + edge.first + " - " + edge.second + ", Weight: " + edge.third);
        }
        
        int totalWeight2 = outputMst2.stream().mapToInt(Triple::getThird).sum();
        System.out.println("最小全域木の合計重み: " + totalWeight2);
        
        // テストケース3
        graphData.clear();
        Object[][] inputList3 = {
            {"A", "B", 4}, {"B", "C", 3}, {"D", "E", 5}
        };
        
        for (Object[] input : inputList3) {
            graphData.addEdge((String)input[0], (String)input[1], (Integer)input[2]);
        }
        
        System.out.println("\nグラフの頂点: " + graphData.getVertices());
        System.out.println("グラフの辺 (重み付き): " + graphData.getEdges());
        
        List<Triple<String, String, Integer>> outputMst3 = graphData.getMst();
        for (Triple<String, String, Integer> edge : outputMst3) {
            System.out.println("Edge: " + edge.first + " - " + edge.second + ", Weight: " + edge.third);
        }
        
        int totalWeight3 = outputMst3.stream().mapToInt(Triple::getThird).sum();
        System.out.println("最小全域木の合計重み: " + totalWeight3);
        
        // テストケース4 (空グラフ)
        graphData.clear();
        
        System.out.println("\nグラフの頂点: " + graphData.getVertices());
        System.out.println("グラフの辺 (重み付き): " + graphData.getEdges());
        
        List<Triple<String, String, Integer>> outputMst4 = graphData.getMst();
        for (Triple<String, String, Integer> edge : outputMst4) {
            System.out.println("Edge: " + edge.first + " - " + edge.second + ", Weight: " + edge.third);
        }
        
        int totalWeight4 = outputMst4.stream().mapToInt(Triple::getThird).sum();
        System.out.println("最小全域木の合計重み: " + totalWeight4);
        
        System.out.println("\nKruskal TEST <----- end");
    }
}