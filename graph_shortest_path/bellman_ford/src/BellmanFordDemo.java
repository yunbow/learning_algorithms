// Java
// グラフの最短経路: ベルマンフォード法 (Bellman Ford)

import java.util.*;


    
class GraphData {
    // 隣接ノードとその辺の重みを格納するマップ
    private Map<String, List<Pair<String, Integer>>> data;
    
    public GraphData() {
        data = new HashMap<>();
    }
    
    public Map<String, List<Pair<String, Integer>>> get() {
        return data;
    }
    
    public List<String> getVertices() {
        return new ArrayList<>(data.keySet());
    }
    
    public List<Triple<String, String, Integer>> getEdges() {
        List<Triple<String, String, Integer>> edges = new ArrayList<>();
        for (String u : data.keySet()) {
            for (Pair<String, Integer> pair : data.get(u)) {
                String v = pair.first;
                int weight = pair.second;
                edges.add(new Triple<>(u, v, weight));
            }
        }
        return edges;
    }
        
    public boolean addVertex(String vertex) {
        if (!data.containsKey(vertex)) {
            data.put(vertex, new ArrayList<>());
        }
        return true;
    }
    
    public boolean addEdge(String vertex1, String vertex2, int weight) {
        addVertex(vertex1);
        addVertex(vertex2);
        
        // vertex1 -> vertex2 の辺を追加
        boolean edgeUpdatedV1V2 = false;
        List<Pair<String, Integer>> neighbors1 = data.get(vertex1);
        for (int i = 0; i < neighbors1.size(); i++) {
            if (neighbors1.get(i).first.equals(vertex2)) {
                neighbors1.set(i, new Pair<>(vertex2, weight));
                edgeUpdatedV1V2 = true;
                break;
            }
        }
        if (!edgeUpdatedV1V2) {
            neighbors1.add(new Pair<>(vertex2, weight));
        }
        
        // vertex2 -> vertex1 の辺を追加
        boolean edgeUpdatedV2V1 = false;
        List<Pair<String, Integer>> neighbors2 = data.get(vertex2);
        for (int i = 0; i < neighbors2.size(); i++) {
            if (neighbors2.get(i).first.equals(vertex1)) {
                neighbors2.set(i, new Pair<>(vertex1, weight));
                edgeUpdatedV2V1 = true;
                break;
            }
        }
        if (!edgeUpdatedV2V1) {
            neighbors2.add(new Pair<>(vertex1, weight));
        }
        
        return true;
    }
    
    public boolean isEmpty() {
        return data.isEmpty();
    }
    
    public boolean clear() {
        data.clear();
        return true;
    }
    
    public Pair<List<String>, Double> getShortestPath(String startVertex, String endVertex, Heuristic heuristic) {
        List<String> vertices = getVertices();
        List<Triple<String, String, Integer>> edges = getEdges();
        int numVertices = vertices.size();
        
        // 始点と終点の存在チェック
        if (!data.containsKey(startVertex)) {
            System.out.println("エラー: 始点 '" + startVertex + "' がグラフに存在しません。");
            return new Pair<>(new ArrayList<>(), Double.POSITIVE_INFINITY);
        }
        if (!data.containsKey(endVertex)) {
            System.out.println("エラー: 終点 '" + endVertex + "' がグラフに存在しません。");
            return new Pair<>(new ArrayList<>(), Double.POSITIVE_INFINITY);
        }
        
        // 始点と終点が同じ場合
        if (startVertex.equals(endVertex)) {
            List<String> path = new ArrayList<>();
            path.add(startVertex);
            return new Pair<>(path, 0.0);
        }
        
        // 距離と先行頂点の初期化
        Map<String, Double> dist = new HashMap<>();
        Map<String, String> pred = new HashMap<>();
        
        for (String vertex : vertices) {
            dist.put(vertex, Double.POSITIVE_INFINITY);
            pred.put(vertex, null);
        }
        dist.put(startVertex, 0.0); // 始点自身の距離は0
        
        // |V| - 1 回の緩和ステップを実行
        for (int i = 0; i < numVertices - 1; i++) {
            boolean relaxedInThisIteration = false;
            for (Triple<String, String, Integer> edge : edges) {
                String u = edge.first;
                String v = edge.second;
                int weight = edge.third;
                
                if (dist.get(u) != Double.POSITIVE_INFINITY && dist.get(u) + weight < dist.get(v)) {
                    dist.put(v, dist.get(u) + weight);
                    pred.put(v, u);
                    relaxedInThisIteration = true;
                }
            }
            
            // このイテレーションで緩和が行われなかった場合はループを抜ける
            if (!relaxedInThisIteration) {
                break;
            }
        }
        
        // 負閉路の検出
        for (Triple<String, String, Integer> edge : edges) {
            String u = edge.first;
            String v = edge.second;
            int weight = edge.third;
            
            if (dist.get(u) != Double.POSITIVE_INFINITY && dist.get(u) + weight < dist.get(v)) {
                System.out.println("エラー: グラフに負閉路が存在します。最短経路は定義できません。");
                return new Pair<>(null, Double.NEGATIVE_INFINITY);
            }
        }
        
        // 最短経路の構築
        List<String> path = new ArrayList<>();
        String current = endVertex;
        
        // 終点まで到達不可能かチェック
        if (dist.get(endVertex) == Double.POSITIVE_INFINITY) {
            return new Pair<>(new ArrayList<>(), Double.POSITIVE_INFINITY);
        }
        
        // 終点から先行頂点をたどって経路を逆順に構築
        while (current != null) {
            path.add(current);
            // 始点に到達したらループを終了
            if (current.equals(startVertex)) {
                break;
            }
            // 次の頂点に進む
            current = pred.get(current);
        }
        
        // 経路が始点から始まっていない場合
        if (path.isEmpty() || !path.get(path.size() - 1).equals(startVertex)) {
            return new Pair<>(new ArrayList<>(), Double.POSITIVE_INFINITY);
        }
        
        // 経路を始点から終点の順にする
        Collections.reverse(path);
        
        return new Pair<>(path, dist.get(endVertex));
    }
}

// ヒューリスティック関数インターフェース
interface Heuristic {
    double estimate(String u, String v);
}

// ダミーヒューリスティック
static class DummyHeuristic implements Heuristic {
    @Override
    public double estimate(String u, String v) {
        return 0;
    }
}

// Pair クラス (2つの値を保持するためのクラス)
static class Pair<A, B> {
    public final A first;
    public final B second;
    
    public Pair(A first, B second) {
        this.first = first;
        this.second = second;
    }
    
    @Override
    public String toString() {
        return "(" + first + ", " + second + ")";
    }
}

// Triple クラス (3つの値を保持するためのクラス)
static class Triple<A, B, C> {
    public final A first;
    public final B second;
    public final C third;
    
    public Triple(A first, B second, C third) {
        this.first = first;
        this.second = second;
        this.third = third;
    }
    
    @Override
    public String toString() {
        return "(" + first + ", " + second + ", " + third + ")";
    }
}
    
public class BellmanFordDemo {
    public static void main(String[] args) {
        System.out.println("BellmanFord TEST -----> start");
        
        GraphData graphData = new GraphData();
        Heuristic dummyHeuristic = new DummyHeuristic();
        
        // テスト1
        graphData.clear();
        Object[][] inputList1 = {{"A", "B", 4}, {"B", "C", 3}, {"B", "D", 2}, {"D", "A", 1}, {"A", "C", 2}, {"B", "D", 2}};
        for (Object[] input : inputList1) {
            graphData.addEdge((String)input[0], (String)input[1], (Integer)input[2]);
        }
        System.out.println("\nグラフの頂点: " + graphData.getVertices());
        System.out.println("グラフの辺 (重み付き): " + graphData.getEdges());
        String[] input1 = {"A", "B"};
        Pair<List<String>, Double> shortestPath1 = graphData.getShortestPath(input1[0], input1[1], dummyHeuristic);
        System.out.println("経路" + input1[0] + "-" + input1[1] + " の最短経路は " + shortestPath1.first + " (重み: " + shortestPath1.second + ")");
        
        // テスト2
        graphData.clear();
        Object[][] inputList2 = {{"A", "B", 4}, {"C", "D", 4}, {"E", "F", 1}, {"F", "G", 1}};
        for (Object[] input : inputList2) {
            graphData.addEdge((String)input[0], (String)input[1], (Integer)input[2]);
        }
        System.out.println("\nグラフの頂点: " + graphData.getVertices());
        System.out.println("グラフの辺 (重み付き): " + graphData.getEdges());
        String[] input2 = {"A", "B"};
        Pair<List<String>, Double> shortestPath2 = graphData.getShortestPath(input2[0], input2[1], dummyHeuristic);
        System.out.println("経路" + input2[0] + "-" + input2[1] + " の最短経路は " + shortestPath2.first + " (重み: " + shortestPath2.second + ")");
        
        // テスト3
        graphData.clear();
        Object[][] inputList3 = {{"A", "B", 4}, {"B", "C", 3}, {"D", "E", 5}};
        for (Object[] input : inputList3) {
            graphData.addEdge((String)input[0], (String)input[1], (Integer)input[2]);
        }
        System.out.println("\nグラフの頂点: " + graphData.getVertices());
        System.out.println("グラフの辺 (重み付き): " + graphData.getEdges());
        String[] input3 = {"A", "D"};
        Pair<List<String>, Double> shortestPath3 = graphData.getShortestPath(input3[0], input3[1], dummyHeuristic);
        System.out.println("経路" + input3[0] + "-" + input3[1] + " の最短経路は " + shortestPath3.first + " (重み: " + shortestPath3.second + ")");
        
        // テスト4
        graphData.clear();
        Object[][] inputList4 = {};
        for (Object[] input : inputList4) {
            // 空のリストなので何も追加されない
        }
        System.out.println("\nグラフの頂点: " + graphData.getVertices());
        System.out.println("グラフの辺 (重み付き): " + graphData.getEdges());
        String[] input4 = {"A", "B"};
        Pair<List<String>, Double> shortestPath4 = graphData.getShortestPath(input4[0], input4[1], dummyHeuristic);
        System.out.println("経路" + input4[0] + "-" + input4[1] + " の最短経路は " + shortestPath4.first + " (重み: " + shortestPath4.second + ")");
        
        System.out.println("\nBellmanFord TEST <----- end");
    }
}