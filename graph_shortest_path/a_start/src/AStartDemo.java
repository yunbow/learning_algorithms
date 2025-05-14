// Java
// グラフの最短経路: A-star

import java.util.*;

class GraphData {
    // 隣接ノードとその辺の重みを格納します
    private Map<String, List<AbstractMap.SimpleEntry<String, Integer>>> data;

    public GraphData() {
        this.data = new HashMap<>();
    }

    public Map<String, List<AbstractMap.SimpleEntry<String, Integer>>> get() {
        // グラフの内部データを取得します
        return this.data;
    }

    public List<String> getVertices() {
        // グラフの全頂点をリストとして返します
        return new ArrayList<>(this.data.keySet());
    }

    public List<AbstractMap.SimpleEntry<AbstractMap.SimpleEntry<String, String>, Integer>> getEdges() {
        // グラフの全辺をリストとして返します
        // 重複を避けるためにセットを使用します
        Set<AbstractMap.SimpleEntry<AbstractMap.SimpleEntry<String, String>, Integer>> edges = new HashSet<>();
        
        for (String vertex : this.data.keySet()) {
            for (AbstractMap.SimpleEntry<String, Integer> neighbor : this.data.get(vertex)) {
                String neighborVertex = neighbor.getKey();
                int weight = neighbor.getValue();
                
                // 辺を正規化してセットに追加（小さい方の頂点を最初にするなど）
                String[] sortedVertices = new String[]{vertex, neighborVertex};
                Arrays.sort(sortedVertices);
                
                AbstractMap.SimpleEntry<String, String> edge = new AbstractMap.SimpleEntry<>(sortedVertices[0], sortedVertices[1]);
                edges.add(new AbstractMap.SimpleEntry<>(edge, weight));
            }
        }
        
        return new ArrayList<>(edges);
    }

    public List<AbstractMap.SimpleEntry<String, Integer>> getNeighbors(String vertex) {
        // 指定された頂点の隣接ノードと辺の重みのリストを返します
        if (this.data.containsKey(vertex)) {
            return this.data.get(vertex);
        } else {
            return null; // 頂点が存在しない場合はNullを返す
        }
    }

    public boolean addVertex(String vertex) {
        // 新しい頂点をグラフに追加します
        if (!this.data.containsKey(vertex)) {
            this.data.put(vertex, new ArrayList<>());
            return true;
        }
        // 既に存在する場合は追加しないがTrueを返す（変更なしでも成功とみなす）
        return true;
    }

    public boolean addEdge(String vertex1, String vertex2, int weight) {
        // 両頂点間に辺を追加します。重みを指定します
        // 頂点がグラフに存在しない場合は追加します
        if (!this.data.containsKey(vertex1)) {
            this.addVertex(vertex1);
        }
        if (!this.data.containsKey(vertex2)) {
            this.addVertex(vertex2);
        }
        
        // vertex1 -> vertex2 の辺を追加（重み付き）
        boolean edgeExistsV1V2 = false;
        List<AbstractMap.SimpleEntry<String, Integer>> neighbors1 = this.data.get(vertex1);
        
        for (int i = 0; i < neighbors1.size(); i++) {
            if (neighbors1.get(i).getKey().equals(vertex2)) {
                neighbors1.set(i, new AbstractMap.SimpleEntry<>(vertex2, weight)); // 既に存在する場合は重みを更新
                edgeExistsV1V2 = true;
                break;
            }
        }
        
        if (!edgeExistsV1V2) {
            neighbors1.add(new AbstractMap.SimpleEntry<>(vertex2, weight));
        }
        
        // vertex2 -> vertex1 の辺を追加（重み付き）
        boolean edgeExistsV2V1 = false;
        List<AbstractMap.SimpleEntry<String, Integer>> neighbors2 = this.data.get(vertex2);
        
        for (int i = 0; i < neighbors2.size(); i++) {
            if (neighbors2.get(i).getKey().equals(vertex1)) {
                neighbors2.set(i, new AbstractMap.SimpleEntry<>(vertex1, weight)); // 既に存在する場合は重みを更新
                edgeExistsV2V1 = true;
                break;
            }
        }
        
        if (!edgeExistsV2V1) {
            neighbors2.add(new AbstractMap.SimpleEntry<>(vertex1, weight));
        }
        
        return true;
    }

    public boolean clear() {
        // グラフを空にします
        this.data.clear();
        return true;
    }

    public AbstractMap.SimpleEntry<List<String>, Integer> getShortestPath(String startVertex, String endVertex, Heuristic heuristic) {
        if (!this.data.containsKey(startVertex) || !this.data.containsKey(endVertex)) {
            System.out.println("ERROR: 開始頂点または終了頂点がグラフに存在しません。");
            return new AbstractMap.SimpleEntry<>(null, Integer.MAX_VALUE);
        }

        if (startVertex.equals(endVertex)) {
            List<String> path = new ArrayList<>();
            path.add(startVertex);
            return new AbstractMap.SimpleEntry<>(path, 0);
        }

        // g_costs: 開始ノードから各ノードまでの既知の最短コスト
        Map<String, Integer> gCosts = new HashMap<>();
        for (String vertex : this.data.keySet()) {
            gCosts.put(vertex, Integer.MAX_VALUE);
        }
        gCosts.put(startVertex, 0);

        // f_costs: g_costs + ヒューリスティックコスト (推定合計コスト)
        Map<String, Integer> fCosts = new HashMap<>();
        for (String vertex : this.data.keySet()) {
            fCosts.put(vertex, Integer.MAX_VALUE);
        }
        fCosts.put(startVertex, heuristic.calculate(startVertex, endVertex));

        // came_from: 最短経路で各ノードの直前のノードを記録
        Map<String, String> cameFrom = new HashMap<>();

        // open_set: 探索すべきノードの優先度キュー (f_cost, node)
        PriorityQueue<AbstractMap.SimpleEntry<Integer, String>> openSet = new PriorityQueue<>(
            Comparator.comparing(AbstractMap.SimpleEntry::getKey)
        );
        openSet.add(new AbstractMap.SimpleEntry<>(fCosts.get(startVertex), startVertex));

        while (!openSet.isEmpty()) {
            // open_set から最も f_cost が低いノードを取り出す
            AbstractMap.SimpleEntry<Integer, String> current = openSet.poll();
            int currentFCost = current.getKey();
            String currentVertex = current.getValue();

            // 取り出したノードの f_cost が、記録されている f_costs[current_vertex] より大きい場合、
            // それは古い情報なので無視して次のノードに進む
            if (currentFCost > fCosts.get(currentVertex)) {
                continue;
            }

            // 目標ノードに到達した場合、経路を再構築して返す
            if (currentVertex.equals(endVertex)) {
                return new AbstractMap.SimpleEntry<>(reconstructPath(cameFrom, endVertex), gCosts.get(endVertex));
            }

            // 現在のノードの隣接ノードを調べる
            List<AbstractMap.SimpleEntry<String, Integer>> neighbors = this.getNeighbors(currentVertex);
            if (neighbors == null) { // 孤立したノードの場合など
                continue;
            }

            for (AbstractMap.SimpleEntry<String, Integer> neighborEntry : neighbors) {
                String neighbor = neighborEntry.getKey();
                int weight = neighborEntry.getValue();
                
                // 現在のノードを経由した場合の隣接ノードへの新しい g_cost
                int tentativeGCost = gCosts.get(currentVertex) + weight;

                // 新しい g_cost が、現在記録されている隣接ノードへの g_cost よりも小さい場合
                if (tentativeGCost < gCosts.get(neighbor)) {
                    // 経路情報を更新
                    cameFrom.put(neighbor, currentVertex);
                    gCosts.put(neighbor, tentativeGCost);
                    fCosts.put(neighbor, gCosts.get(neighbor) + heuristic.calculate(neighbor, endVertex));
                    
                    // 隣接ノードを open_set に追加（または優先度を更新）
                    openSet.add(new AbstractMap.SimpleEntry<>(fCosts.get(neighbor), neighbor));
                }
            }
        }

        // open_set が空になっても目標ノードに到達しなかった場合、経路は存在しない
        return new AbstractMap.SimpleEntry<>(null, Integer.MAX_VALUE);
    }

    private List<String> reconstructPath(Map<String, String> cameFrom, String currentVertex) {
        List<String> path = new ArrayList<>();
        while (cameFrom.containsKey(currentVertex)) {
            path.add(currentVertex);
            currentVertex = cameFrom.get(currentVertex);
        }
        path.add(currentVertex); // 開始ノードを追加
        Collections.reverse(path); // 経路を逆順にする (開始 -> 目標)
        return path;
    }
}

// ヒューリスティック関数のインターフェース
interface Heuristic {
    int calculate(String u, String v);
}

// ダミーヒューリスティック（常に0を返す）
class DummyHeuristic implements Heuristic {
    @Override
    public int calculate(String u, String v) {
        // u と v の間に何らかの推定距離を計算する関数
        // ここではダミーとして常に0を返す
        return 0;
    }
}

public class AStartDemo {
    public static void main(String[] args) {
        System.out.println("A-start TEST -----> start");

        GraphData graphData = new GraphData();
        Heuristic dummyHeuristic = new DummyHeuristic();

        graphData.clear();
        String[][] inputList = {{"A", "B", "4"}, {"B", "C", "3"}, {"B", "D", "2"}, {"D", "A", "1"}, {"A", "C", "2"}, {"B", "D", "2"}};
        for (String[] input : inputList) {
            graphData.addEdge(input[0], input[1], Integer.parseInt(input[2]));
        }
        System.out.println("\nグラフの頂点: " + graphData.getVertices());
        System.out.println("グラフの辺 (重み付き): " + graphData.getEdges());
        String[] input = {"A", "B"};
        AbstractMap.SimpleEntry<List<String>, Integer> shortestPath = graphData.getShortestPath(input[0], input[1], dummyHeuristic);
        System.out.println("経路" + input[0] + "-" + input[1] + " の最短経路は " + shortestPath.getKey() + " (重み: " + shortestPath.getValue() + ")");

        graphData.clear();
        inputList = new String[][]{{"A", "B", "4"}, {"C", "D", "4"}, {"E", "F", "1"}, {"F", "G", "1"}};
        for (String[] inputItem : inputList) {
            graphData.addEdge(inputItem[0], inputItem[1], Integer.parseInt(inputItem[2]));
        }
        System.out.println("\nグラフの頂点: " + graphData.getVertices());
        System.out.println("グラフの辺 (重み付き): " + graphData.getEdges());
        input = new String[]{"A", "B"};
        shortestPath = graphData.getShortestPath(input[0], input[1], dummyHeuristic);
        System.out.println("経路" + input[0] + "-" + input[1] + " の最短経路は " + shortestPath.getKey() + " (重み: " + shortestPath.getValue() + ")");

        graphData.clear();
        inputList = new String[][]{{"A", "B", "4"}, {"B", "C", "3"}, {"D", "E", "5"}};
        for (String[] inputItem : inputList) {
            graphData.addEdge(inputItem[0], inputItem[1], Integer.parseInt(inputItem[2]));
        }
        System.out.println("\nグラフの頂点: " + graphData.getVertices());
        System.out.println("グラフの辺 (重み付き): " + graphData.getEdges());
        input = new String[]{"A", "D"};
        shortestPath = graphData.getShortestPath(input[0], input[1], dummyHeuristic);
        System.out.println("経路" + input[0] + "-" + input[1] + " の最短経路は " + shortestPath.getKey() + " (重み: " + shortestPath.getValue() + ")");

        graphData.clear();
        inputList = new String[][]{};
        for (String[] inputItem : inputList) {
            graphData.addEdge(inputItem[0], inputItem[1], Integer.parseInt(inputItem[2]));
        }
        System.out.println("\nグラフの頂点: " + graphData.getVertices());
        System.out.println("グラフの辺 (重み付き): " + graphData.getEdges());
        input = new String[]{"A", "B"};
        shortestPath = graphData.getShortestPath(input[0], input[1], dummyHeuristic);
        System.out.println("経路" + input[0] + "-" + input[1] + " の最短経路は " + shortestPath.getKey() + " (重み: " + shortestPath.getValue() + ")");

        System.out.println("\nA-start TEST <----- end");
    }
}