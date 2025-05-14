// Java
// グラフの最短経路: ワーシャルフロイド法 (Warshall Floyd)

import java.util.*;

public class WarshallFloydDemo {
    
    static class GraphData {
        // 隣接ノードとその辺の重みを格納します。
        // キーは頂点、値はその頂点に隣接する頂点と重みのリストです。
        private Map<String, List<AbstractMap.SimpleEntry<String, Integer>>> data;
        
        public GraphData() {
            data = new HashMap<>();
        }
        
        public Map<String, List<AbstractMap.SimpleEntry<String, Integer>>> get() {
            // グラフの内部データを取得します。
            return data;
        }
        
        public List<String> getVertices() {
            // グラフの全頂点をリストとして返します。
            return new ArrayList<>(data.keySet());
        }
        
        public List<Object[]> getEdges() {
            // グラフの全辺をリストとして返します。
            // 無向グラフの場合、(u, v, weight) の形式で返します。
            // 重複を避けるためにセットを使用します。
            Set<Object[]> edges = new HashSet<>();
            for (String vertex : data.keySet()) {
                if (data.get(vertex) != null) {
                    for (AbstractMap.SimpleEntry<String, Integer> neighbor : data.get(vertex)) {
                        // 辺を正規化してセットに追加 (小さい方の頂点を最初にするなど)
                        String[] sortedEdge = new String[]{vertex, neighbor.getKey()};
                        Arrays.sort(sortedEdge);
                        // (u, v, weight) の形式で格納
                        edges.add(new Object[]{sortedEdge[0], sortedEdge[1], neighbor.getValue()});
                    }
                }
            }
            return new ArrayList<>(edges);
        }
        
        public List<AbstractMap.SimpleEntry<String, Integer>> getNeighbors(String vertex) {
            // 指定された頂点の隣接ノードと辺の重みのリストを返します。
            // 形式: [(隣接頂点, 重み), ...]
            if (data.containsKey(vertex)) {
                return data.get(vertex);
            } else {
                return null; // 頂点が存在しない場合はNullを返す
            }
        }
        
        public boolean addVertex(String vertex) {
            // 新しい頂点をグラフに追加します。
            if (!data.containsKey(vertex)) {
                data.put(vertex, new ArrayList<>());
                return true;
            }
            // 既に存在する場合は追加しないがTrueを返す（変更なしでも成功とみなす）
            return true;
        }
        
        public boolean addEdge(String vertex1, String vertex2, int weight) {
            // 両頂点間に辺を追加します。重みを指定します。
            // 頂点がグラフに存在しない場合は追加します。
            if (!data.containsKey(vertex1)) {
                addVertex(vertex1);
            }
            if (!data.containsKey(vertex2)) {
                addVertex(vertex2);
            }
            
            // 両方向に辺を追加する（無向グラフ）
            
            // vertex1 -> vertex2 の辺を追加（重み付き）
            boolean edgeExistsV1V2 = false;
            for (int i = 0; i < data.get(vertex1).size(); i++) {
                AbstractMap.SimpleEntry<String, Integer> edge = data.get(vertex1).get(i);
                if (edge.getKey().equals(vertex2)) {
                    data.get(vertex1).set(i, new AbstractMap.SimpleEntry<>(vertex2, weight)); // 既に存在する場合は重みを更新
                    edgeExistsV1V2 = true;
                    break;
                }
            }
            if (!edgeExistsV1V2) {
                data.get(vertex1).add(new AbstractMap.SimpleEntry<>(vertex2, weight));
            }
            
            // vertex2 -> vertex1 の辺を追加（重み付き）
            boolean edgeExistsV2V1 = false;
            for (int i = 0; i < data.get(vertex2).size(); i++) {
                AbstractMap.SimpleEntry<String, Integer> edge = data.get(vertex2).get(i);
                if (edge.getKey().equals(vertex1)) {
                    data.get(vertex2).set(i, new AbstractMap.SimpleEntry<>(vertex1, weight)); // 既に存在する場合は重みを更新
                    edgeExistsV2V1 = true;
                    break;
                }
            }
            if (!edgeExistsV2V1) {
                data.get(vertex2).add(new AbstractMap.SimpleEntry<>(vertex1, weight));
            }
            
            return true;
        }
        
        public boolean clear() {
            // グラフを空にします。
            data.clear();
            return true;
        }
        
        public Object[] getShortestPath(String startVertex, String endVertex, Heuristic heuristic) {
            List<String> vertices = getVertices();
            int numVertices = vertices.size();
            if (numVertices == 0) {
                return new Object[]{null, Double.POSITIVE_INFINITY};
            }
            
            // 頂点名をインデックスにマッピング
            Map<String, Integer> vertexToIndex = new HashMap<>();
            Map<Integer, String> indexToVertex = new HashMap<>();
            
            for (int i = 0; i < vertices.size(); i++) {
                String vertex = vertices.get(i);
                vertexToIndex.put(vertex, i);
                indexToVertex.put(i, vertex);
            }
            
            // 開始・終了頂点が存在するか確認
            if (!vertexToIndex.containsKey(startVertex) || !vertexToIndex.containsKey(endVertex)) {
                System.out.println("ERROR: " + startVertex + " または " + endVertex + " がグラフに存在しません。");
                return new Object[]{null, Double.POSITIVE_INFINITY};
            }
            
            int startIndex = vertexToIndex.get(startVertex);
            int endIndex = vertexToIndex.get(endVertex);
            
            // 距離行列 (dist) と経路復元用行列 (next_node) を初期化
            double INF = Double.POSITIVE_INFINITY;
            double[][] dist = new double[numVertices][numVertices];
            Integer[][] nextNode = new Integer[numVertices][numVertices];
            
            // すべての距離を初期化
            for (int i = 0; i < numVertices; i++) {
                Arrays.fill(dist[i], INF);
                dist[i][i] = 0; // 自分自身への距離は0
            }
            
            // 初期距離と経路復元情報を設定
            for (int i = 0; i < numVertices; i++) {
                String vertex = vertices.get(i);
                List<AbstractMap.SimpleEntry<String, Integer>> neighbors = getNeighbors(vertex);
                if (neighbors != null) {
                    for (AbstractMap.SimpleEntry<String, Integer> edge : neighbors) {
                        String neighbor = edge.getKey();
                        int weight = edge.getValue();
                        int j = vertexToIndex.get(neighbor);
                        dist[i][j] = weight;
                        nextNode[i][j] = j; // iからjへの直接辺の場合、iの次はj
                    }
                }
            }
            
            // ワーシャル-フロイド法の本体
            // k: 中継点として使用する頂点のインデックス
            for (int k = 0; k < numVertices; k++) {
                // i: 開始頂点のインデックス
                for (int i = 0; i < numVertices; i++) {
                    // j: 終了頂点のインデックス
                    for (int j = 0; j < numVertices; j++) {
                        // i -> k -> j の経路が i -> j の現在の経路より短い場合
                        if (dist[i][k] != INF && dist[k][j] != INF && dist[i][k] + dist[k][j] < dist[i][j]) {
                            dist[i][j] = dist[i][k] + dist[k][j];
                            nextNode[i][j] = nextNode[i][k]; // iからjへの最短経路で、iの次の頂点はiからkへの最短経路でのiの次の頂点
                        }
                    }
                }
            }
            
            // 指定された開始・終了頂点間の最短経路と重みを取得
            double shortestDistance = dist[startIndex][endIndex];
            
            // 経路が存在しない場合 (距離がINF)
            if (shortestDistance == INF) {
                return new Object[]{null, INF};
            }
            
            // 経路を復元
            List<String> path = new ArrayList<>();
            int u = startIndex;
            // 開始と終了が同じ場合は経路は開始頂点のみ
            if (u == endIndex) {
                path.add(startVertex);
            } else {
                // next_nodeを使って経路をたどる
                while (u != -1 && u != endIndex) {
                    path.add(indexToVertex.get(u));
                    u = nextNode[u][endIndex] != null ? nextNode[u][endIndex] : -1;
                    // 無限ループ防止のための簡易チェック
                    if (u != -1 && !path.isEmpty() && indexToVertex.get(u).equals(path.get(path.size() - 1))) {
                        // 同じ頂点に戻ってきたなど、異常な経路復元を防ぐ
                        System.out.println("WARNING: 経路復元中に異常を検出しました（" + indexToVertex.get(u) + "でループ？）。");
                        path = null;
                        shortestDistance = INF;
                        break;
                    }
                }
                // 最後のノード (end_vertex) を追加
                if (path != null) {
                    path.add(endVertex);
                }
            }
            
            return new Object[]{path, shortestDistance};
        }
    }
    
    // ヒューリスティック関数インターフェース
    interface Heuristic {
        double estimate(String u, String v);
    }
    
    // ダミーヒューリスティック（常に0を返す）
    static class DummyHeuristic implements Heuristic {
        @Override
        public double estimate(String u, String v) {
            // u と v の間に何らかの推定距離を計算する関数
            // ここではダミーとして常に0を返す
            return 0;
        }
    }
    
    public static void main(String[] args) {
        System.out.println("WarshallFloyd -----> start");
        
        GraphData graphData = new GraphData();
        Heuristic dummyHeuristic = new DummyHeuristic();
        
        graphData.clear();
        Object[][] inputList = {{"A", "B", 4}, {"B", "C", 3}, {"B", "D", 2}, {"D", "A", 1}, {"A", "C", 2}, {"B", "D", 2}};
        for (Object[] input : inputList) {
            graphData.addEdge((String)input[0], (String)input[1], (Integer)input[2]);
        }
        System.out.println("\nグラフの頂点: " + graphData.getVertices());
        System.out.println("グラフの辺 (重み付き): " + formatEdges(graphData.getEdges()));
        String[] input = {"A", "B"};
        Object[] shortestPath = graphData.getShortestPath(input[0], input[1], dummyHeuristic);
        System.out.println("経路" + input[0] + "-" + input[1] + " の最短経路は " + shortestPath[0] + " (重み: " + shortestPath[1] + ")");
        
        graphData.clear();
        inputList = new Object[][]{{"A", "B", 4}, {"C", "D", 4}, {"E", "F", 1}, {"F", "G", 1}};
        for (Object[] in : inputList) {
            graphData.addEdge((String)in[0], (String)in[1], (Integer)in[2]);
        }
        System.out.println("\nグラフの頂点: " + graphData.getVertices());
        System.out.println("グラフの辺 (重み付き): " + formatEdges(graphData.getEdges()));
        input = new String[]{"A", "B"};
        shortestPath = graphData.getShortestPath(input[0], input[1], dummyHeuristic);
        System.out.println("経路" + input[0] + "-" + input[1] + " の最短経路は " + shortestPath[0] + " (重み: " + shortestPath[1] + ")");
        
        graphData.clear();
        inputList = new Object[][]{{"A", "B", 4}, {"B", "C", 3}, {"D", "E", 5}};
        for (Object[] in : inputList) {
            graphData.addEdge((String)in[0], (String)in[1], (Integer)in[2]);
        }
        System.out.println("\nグラフの頂点: " + graphData.getVertices());
        System.out.println("グラフの辺 (重み付き): " + formatEdges(graphData.getEdges()));
        input = new String[]{"A", "D"};
        shortestPath = graphData.getShortestPath(input[0], input[1], dummyHeuristic);
        System.out.println("経路" + input[0] + "-" + input[1] + " の最短経路は " + shortestPath[0] + " (重み: " + shortestPath[1] + ")");
        
        graphData.clear();
        inputList = new Object[][]{};
        for (Object[] in : inputList) {
            if (in.length >= 3) {
                graphData.addEdge((String)in[0], (String)in[1], (Integer)in[2]);
            }
        }
        System.out.println("\nグラフの頂点: " + graphData.getVertices());
        System.out.println("グラフの辺 (重み付き): " + formatEdges(graphData.getEdges()));
        input = new String[]{"A", "B"};
        shortestPath = graphData.getShortestPath(input[0], input[1], dummyHeuristic);
        System.out.println("経路" + input[0] + "-" + input[1] + " の最短経路は " + shortestPath[0] + " (重み: " + shortestPath[1] + ")");
        
        System.out.println("\nWarshallFloyd <----- end");
    }
    
    // エッジのリストを整形して表示するためのヘルパーメソッド
    private static String formatEdges(List<Object[]> edges) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < edges.size(); i++) {
            Object[] edge = edges.get(i);
            sb.append("(").append(edge[0]).append(", ").append(edge[1]).append(", ").append(edge[2]).append(")");
            if (i < edges.size() - 1) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }
}