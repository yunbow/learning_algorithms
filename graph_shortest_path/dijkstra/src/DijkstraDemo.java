// Java
// グラフの最短経路: ダイクストラ法 (dijkstra)

import java.util.*;

/**
 * キーと値のペアを表すクラス
 */
class Pair<K, V> {
    private final K first;
    private final V second;

    public Pair(K first, V second) {
        this.first = first;
        this.second = second;
    }

    public K getFirst() {
        return first;
    }

    public V getSecond() {
        return second;
    }

    @Override
    public String toString() {
        return "(" + first + ", " + second + ")";
    }
}

/**
 * グラフデータを管理するクラス
 */
class GraphData {
    // 隣接ノードとその辺の重みを格納します。
    // キーは頂点、値はその頂点に隣接する頂点と重みのリストです。
    private Map<String, List<Pair<String, Double>>> data;

    public GraphData() {
        data = new HashMap<>();
    }

    /**
     * グラフの内部データを取得します。
     */
    public Map<String, List<Pair<String, Double>>> get() {
        return data;
    }

    /**
     * グラフの全頂点をリストとして返します。
     */
    public List<String> getVertices() {
        return new ArrayList<>(data.keySet());
    }

    /**
     * グラフの全辺をリストとして返します。
     * 無向グラフの場合、(u, v, weight) の形式で返します。
     */
    public List<Object[]> getEdges() {
        Set<String> processedEdges = new HashSet<>();
        List<Object[]> edges = new ArrayList<>();

        for (String vertex : data.keySet()) {
            for (Pair<String, Double> neighborInfo : data.get(vertex)) {
                String neighbor = neighborInfo.getFirst();
                double weight = neighborInfo.getSecond();

                // 辺を正規化して重複を避ける (小さい方の頂点を最初にする)
                String normalizedEdge = vertex.compareTo(neighbor) < 0 ? 
                    vertex + "," + neighbor : neighbor + "," + vertex;

                if (!processedEdges.contains(normalizedEdge)) {
                    processedEdges.add(normalizedEdge);
                    // (u, v, weight) の形式で格納
                    edges.add(new Object[]{vertex, neighbor, weight});
                }
            }
        }
        return edges;
    }

    /**
     * 指定された頂点の隣接ノードと辺の重みのリストを返します。
     */
    public List<Pair<String, Double>> getNeighbors(String vertex) {
        if (data.containsKey(vertex)) {
            return data.get(vertex);
        } else {
            return null; // 頂点が存在しない場合はNullを返す
        }
    }

    /**
     * 新しい頂点をグラフに追加します。
     */
    public boolean addVertex(String vertex) {
        if (!data.containsKey(vertex)) {
            data.put(vertex, new ArrayList<>());
        }
        return true;
    }

    /**
     * 両頂点間に辺を追加します。重みを指定します。
     */
    public boolean addEdge(String vertex1, String vertex2, double weight) {
        if (!data.containsKey(vertex1)) {
            addVertex(vertex1);
        }
        if (!data.containsKey(vertex2)) {
            addVertex(vertex2);
        }

        // vertex1 -> vertex2 の辺を追加（重み付き）
        boolean edgeExistsV1V2 = false;
        for (int i = 0; i < data.get(vertex1).size(); i++) {
            Pair<String, Double> edge = data.get(vertex1).get(i);
            if (edge.getFirst().equals(vertex2)) {
                data.get(vertex1).set(i, new Pair<>(vertex2, weight)); // 既に存在する場合は重みを更新
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
            Pair<String, Double> edge = data.get(vertex2).get(i);
            if (edge.getFirst().equals(vertex1)) {
                data.get(vertex2).set(i, new Pair<>(vertex1, weight)); // 既に存在する場合は重みを更新
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
     * グラフを空にします。
     */
    public boolean clear() {
        data = new HashMap<>();
        return true;
    }

    /**
     * 最短経路を計算する関数
     */
    public Pair<List<String>, Double> getShortestPath(String startVertex, String endVertex, 
                                                      java.util.function.BiFunction<String, String, Double> heuristic) {
        if (!data.containsKey(startVertex) || !data.containsKey(endVertex)) {
            System.out.println("ERROR: 開始頂点 '" + startVertex + "' または 終了頂点 '" + endVertex + "' がグラフに存在しません。");
            return new Pair<>(null, Double.POSITIVE_INFINITY);
        }

        // 距離を初期化: 全ての頂点への距離を無限大に設定し、開始頂点のみ0とする
        Map<String, Double> distances = new HashMap<>();
        for (String vertex : data.keySet()) {
            distances.put(vertex, Double.POSITIVE_INFINITY);
        }
        distances.put(startVertex, 0.0);

        // 経路を記録するためのマップ: 各頂点に至る直前の頂点を保持
        Map<String, String> predecessors = new HashMap<>();
        for (String vertex : data.keySet()) {
            predecessors.put(vertex, null);
        }

        // 優先度付きキュー: (距離, 頂点) のペアを格納し、距離が小さい順に取り出す
        PriorityQueue<Pair<Double, String>> priorityQueue = new PriorityQueue<>(
            Comparator.comparing(Pair::getFirst)
        );
        priorityQueue.add(new Pair<>(0.0, startVertex));

        while (!priorityQueue.isEmpty()) {
            // 優先度付きキューから最も距離の小さい頂点を取り出す
            Pair<Double, String> current = priorityQueue.poll();
            double currentDistance = current.getFirst();
            String currentVertex = current.getSecond();

            // 取り出した頂点への距離が、すでに記録されている距離より大きい場合は、
            // より短い経路が既に見つかっているためスキップ
            if (currentDistance > distances.get(currentVertex)) {
                continue;
            }

            // 終了頂点に到達したら探索終了
            if (currentVertex.equals(endVertex)) {
                break; // 最短経路が見つかった
            }

            // 現在の頂点から到達可能な隣接頂点を探索
            for (Pair<String, Double> neighborInfo : getNeighbors(currentVertex)) {
                String neighbor = neighborInfo.getFirst();
                double weight = neighborInfo.getSecond();
                double distanceThroughCurrent = distances.get(currentVertex) + weight;

                // より短い経路が見つかった場合
                if (distanceThroughCurrent < distances.get(neighbor)) {
                    distances.put(neighbor, distanceThroughCurrent);
                    predecessors.put(neighbor, currentVertex);
                    // 優先度付きキューに隣接頂点を追加または更新
                    priorityQueue.add(new Pair<>(distanceThroughCurrent, neighbor));
                }
            }
        }

        // 終了頂点への最短距離が無限大のままなら、到達不可能
        if (distances.get(endVertex) == Double.POSITIVE_INFINITY) {
            System.out.println("INFO: 開始頂点 '" + startVertex + "' から 終了頂点 '" + endVertex + "' への経路は存在しません。");
            return new Pair<>(null, Double.POSITIVE_INFINITY);
        }

        // 最短経路を再構築
        List<String> path = new ArrayList<>();
        String current = endVertex;
        while (current != null) {
            path.add(current);
            current = predecessors.get(current);
        }
        Collections.reverse(path); // 経路は逆順に構築されたので反転

        // 開始ノードから開始されていることを確認
        if (!path.get(0).equals(startVertex)) {
            return new Pair<>(null, Double.POSITIVE_INFINITY);
        }

        return new Pair<>(path, distances.get(endVertex));
    }
}

public class DijkstraDemo {
    public static void main(String[] args) {
        System.out.println("Dijkstra -----> start");

        GraphData graphData = new GraphData();

        graphData.clear();
        Object[][] inputList = {{"A", "B", 4}, {"B", "C", 3}, {"B", "D", 2}, {"D", "A", 1}, {"A", "C", 2}, {"B", "D", 2}};
        for (Object[] input : inputList) {
            graphData.addEdge((String)input[0], (String)input[1], (Integer)input[2]);
        }
        System.out.println("\nグラフの頂点: " + graphData.getVertices());
        System.out.println("グラフの辺 (重み付き): " + graphData.getEdges());
        String[] input = {"A", "B"};
        Pair<List<String>, Double> shortestPath = graphData.getShortestPath(input[0], input[1], DijkstraDemo::dummyHeuristic);
        System.out.println("経路" + input[0] + "-" + input[1] + " の最短経路は " + shortestPath.getFirst() + " (重み: " + shortestPath.getSecond() + ")");

        graphData.clear();
        Object[][] inputList2 = {{"A", "B", 4}, {"C", "D", 4}, {"E", "F", 1}, {"F", "G", 1}};
        for (Object[] input2 : inputList2) {
            graphData.addEdge((String)input2[0], (String)input2[1], (Integer)input2[2]);
        }
        System.out.println("\nグラフの頂点: " + graphData.getVertices());
        System.out.println("グラフの辺 (重み付き): " + graphData.getEdges());
        String[] input2 = {"A", "B"};
        Pair<List<String>, Double> shortestPath2 = graphData.getShortestPath(input2[0], input2[1], DijkstraDemo::dummyHeuristic);
        System.out.println("経路" + input2[0] + "-" + input2[1] + " の最短経路は " + shortestPath2.getFirst() + " (重み: " + shortestPath2.getSecond() + ")");

        graphData.clear();
        Object[][] inputList3 = {{"A", "B", 4}, {"B", "C", 3}, {"D", "E", 5}};
        for (Object[] input3 : inputList3) {
            graphData.addEdge((String)input3[0], (String)input3[1], (Integer)input3[2]);
        }
        System.out.println("\nグラフの頂点: " + graphData.getVertices());
        System.out.println("グラフの辺 (重み付き): " + graphData.getEdges());
        String[] input3 = {"A", "D"};
        Pair<List<String>, Double> shortestPath3 = graphData.getShortestPath(input3[0], input3[1], DijkstraDemo::dummyHeuristic);
        System.out.println("経路" + input3[0] + "-" + input3[1] + " の最短経路は " + shortestPath3.getFirst() + " (重み: " + shortestPath3.getSecond() + ")");

        graphData.clear();
        Object[][] inputList4 = {};
        for (Object[] input4 : inputList4) {
            graphData.addEdge((String)input4[0], (String)input4[1], (Integer)input4[2]);
        }
        System.out.println("\nグラフの頂点: " + graphData.getVertices());
        System.out.println("グラフの辺 (重み付き): " + graphData.getEdges());
        String[] input4 = {"A", "B"};
        Pair<List<String>, Double> shortestPath4 = graphData.getShortestPath(input4[0], input4[1], DijkstraDemo::dummyHeuristic);
        System.out.println("経路" + input4[0] + "-" + input4[1] + " の最短経路は " + shortestPath4.getFirst() + " (重み: " + shortestPath4.getSecond() + ")");

        System.out.println("\nDijkstra <----- end");
    }

    // ヒューリスティック関数 (この例では常に0、ダイクストラ法と同じ)
    public static double dummyHeuristic(String u, String v) {
        // u と v の間に何らかの推定距離を計算する関数
        // ここではダミーとして常に0を返す
        return 0;
    }
}