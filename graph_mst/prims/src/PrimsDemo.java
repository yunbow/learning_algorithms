// Java
// グラフの最小全域木: プリム法 (Prim)

import java.util.*;

class GraphData {
    // 隣接リストとしてグラフデータを格納します。
    // キーは頂点、値はその頂点に隣接する頂点とその辺の重みのマップのリストです。
    private Map<String, List<Map.Entry<String, Integer>>> data;

    public GraphData() {
        this.data = new HashMap<>();
    }

    public Map<String, List<Map.Entry<String, Integer>>> get() {
        // グラフの内部データを取得します。
        return this.data;
    }

    public List<String> getVertices() {
        // グラフの全頂点をリストとして返します。
        return new ArrayList<>(this.data.keySet());
    }

    public List<Object[]> getEdges() {
        // グラフの全辺をリストとして返します。
        // 無向グラフの場合、[u, v, weight] の形式で返します。
        // 重複を避けるためにセットを使用します。
        Set<List<Object>> edges = new HashSet<>();
        for (String vertex : this.data.keySet()) {
            for (Map.Entry<String, Integer> neighbor : this.data.get(vertex)) {
                String neighborVertex = neighbor.getKey();
                Integer weight = neighbor.getValue();
                // 辺を正規化してセットに追加 (小さい方の頂点を最初にするなど)
                List<String> edge = Arrays.asList(vertex, neighborVertex);
                Collections.sort(edge);
                edges.add(Arrays.asList(edge.get(0), edge.get(1), weight));
            }
        }
        
        // Set<List<Object>>をList<Object[]>に変換
        List<Object[]> result = new ArrayList<>();
        for (List<Object> edge : edges) {
            result.add(new Object[]{edge.get(0), edge.get(1), edge.get(2)});
        }
        return result;
    }

    public List<Map.Entry<String, Integer>> getNeighbors(String vertex) {
        // 指定された頂点の隣接ノードと辺の重みのリストを返します。
        if (this.data.containsKey(vertex)) {
            return this.data.get(vertex);
        } else {
            return null; // 頂点が存在しない場合はnullを返す
        }
    }

    public Integer getEdgeWeight(String vertex1, String vertex2) {
        // 指定された2つの頂点間の辺の重みを返します。
        // 辺が存在しない場合はnullを返します。
        if (this.data.containsKey(vertex1) && this.data.containsKey(vertex2)) {
            for (Map.Entry<String, Integer> neighbor : this.data.get(vertex1)) {
                if (neighbor.getKey().equals(vertex2)) {
                    return neighbor.getValue();
                }
            }
        }
        return null; // 辺が存在しない場合
    }

    public boolean addVertex(String vertex) {
        // 新しい頂点をグラフに追加します。
        if (!this.data.containsKey(vertex)) {
            this.data.put(vertex, new ArrayList<>());
            return true;
        }
        // 既に存在する場合は追加しないがtrueを返す（変更なしでも成功とみなす）
        return true;
    }

    public boolean addEdge(String vertex1, String vertex2, int weight) {
        // 両頂点間に辺を追加します。重みを指定します。
        // 頂点がグラフに存在しない場合は追加します。
        if (!this.data.containsKey(vertex1)) {
            this.addVertex(vertex1);
        }
        if (!this.data.containsKey(vertex2)) {
            this.addVertex(vertex2);
        }

        // vertex1 -> vertex2 の辺を追加（重み付き）
        boolean edgeExistsV1V2 = false;
        for (int i = 0; i < this.data.get(vertex1).size(); i++) {
            Map.Entry<String, Integer> entry = this.data.get(vertex1).get(i);
            if (entry.getKey().equals(vertex2)) {
                // 既に存在する場合は重みを更新
                this.data.get(vertex1).set(i, new AbstractMap.SimpleEntry<>(vertex2, weight));
                edgeExistsV1V2 = true;
                break;
            }
        }
        if (!edgeExistsV1V2) {
            this.data.get(vertex1).add(new AbstractMap.SimpleEntry<>(vertex2, weight));
        }

        // vertex2 -> vertex1 の辺を追加（重み付き）
        boolean edgeExistsV2V1 = false;
        for (int i = 0; i < this.data.get(vertex2).size(); i++) {
            Map.Entry<String, Integer> entry = this.data.get(vertex2).get(i);
            if (entry.getKey().equals(vertex1)) {
                // 既に存在する場合は重みを更新
                this.data.get(vertex2).set(i, new AbstractMap.SimpleEntry<>(vertex1, weight));
                edgeExistsV2V1 = true;
                break;
            }
        }
        if (!edgeExistsV2V1) {
            this.data.get(vertex2).add(new AbstractMap.SimpleEntry<>(vertex1, weight));
        }

        return true;
    }

    public boolean isEmpty() {
        // グラフが空かどうかを返します。
        return this.data.isEmpty();
    }

    public boolean clear() {
        // グラフを空にします。
        this.data = new HashMap<>();
        return true;
    }

    public List<Object[]> getMst(String startVertex) {
        List<String> vertices = this.getVertices();
        if (vertices.isEmpty()) {
            return new ArrayList<>(); // グラフが空
        }

        if (startVertex == null) {
            startVertex = vertices.get(0);
        } else if (!this.data.containsKey(startVertex)) {
            System.out.println("ERROR: 開始頂点 " + startVertex + " はグラフに存在しません。");
            return null;
        }

        // MSTに含まれる頂点のセット
        Set<String> inMst = new HashSet<>();
        // 優先度付きキュー (重み, 現在の頂点, 遷移元の頂点)
        // PriorityQueueは最小ヒープで、重みが小さい辺から取り出される
        PriorityQueue<Object[]> minHeap = new PriorityQueue<>(Comparator.comparingInt(a -> (Integer) a[0]));
        
        // MSTを構成する辺のリスト
        List<Object[]> mstEdges = new ArrayList<>();
        
        // 各頂点への最小コスト（MSTに追加する際の辺の重み）と、その遷移元の頂点を記録
        Map<String, Integer> minCost = new HashMap<>();
        Map<String, String> parent = new HashMap<>();
        
        for (String v : vertices) {
            minCost.put(v, Integer.MAX_VALUE);
            parent.put(v, null);
        }

        // 開始頂点の処理
        minCost.put(startVertex, 0);
        minHeap.add(new Object[]{0, startVertex, null}); // (コスト, 現在の頂点, 遷移元の頂点)

        while (!minHeap.isEmpty()) {
            // 最小コストの辺を持つ頂点を取り出す
            Object[] current = minHeap.poll();
            int cost = (Integer) current[0];
            String currentVertex = (String) current[1];
            String fromVertex = (String) current[2];

            // 既にMSTに含まれている頂点であればスキップ
            if (inMst.contains(currentVertex)) {
                continue;
            }

            // 現在の頂点をMSTに追加
            inMst.add(currentVertex);

            // MSTに追加された辺を記録 (開始頂点以外)
            if (fromVertex != null) {
                // fromVertex から currentVertex への辺の重みを取得
                Integer weight = this.getEdgeWeight(fromVertex, currentVertex);
                if (weight != null) {
                    // 辺を正規化して追加
                    List<String> edge = Arrays.asList(fromVertex, currentVertex);
                    Collections.sort(edge);
                    mstEdges.add(new Object[]{edge.get(0), edge.get(1), weight});
                }
            }

            // 現在の頂点に隣接する頂点を調べ、MSTへの追加コストを更新
            List<Map.Entry<String, Integer>> neighborsWithWeight = this.getNeighbors(currentVertex);
            if (neighborsWithWeight != null) { // 隣接する頂点がある場合
                for (Map.Entry<String, Integer> neighborEntry : neighborsWithWeight) {
                    String neighbor = neighborEntry.getKey();
                    int weight = neighborEntry.getValue();
                    // 隣接頂点がまだMSTに含まれておらず、現在のコストよりも小さい場合
                    if (!inMst.contains(neighbor) && weight < minCost.get(neighbor)) {
                        minCost.put(neighbor, weight);
                        parent.put(neighbor, currentVertex);
                        minHeap.add(new Object[]{weight, neighbor, currentVertex});
                    }
                }
            }
        }

        return mstEdges;
    }
    
    public List<Object[]> getMst() {
        return getMst(null);
    }
}


public class PrimsDemo {
        public static void main(String[] args) {
        System.out.println("Prims TEST -----> start");
        GraphData graphData = new GraphData();

        graphData.clear();
        Object[][] inputList = {
            {"A", "B", 4}, {"B", "C", 3}, {"B", "D", 2}, {"D", "A", 1}, {"A", "C", 2}, {"B", "D", 2}
        };
        for (Object[] input : inputList) {
            graphData.addEdge((String)input[0], (String)input[1], (Integer)input[2]);
        }
        System.out.println("\nグラフの頂点: " + graphData.getVertices());
        System.out.println("グラフの辺 (重み付き): " + Arrays.deepToString(graphData.getEdges().toArray()));
        List<Object[]> outputMst = graphData.getMst();
        for (Object[] edge : outputMst) {
            System.out.println("Edge: " + edge[0] + " - " + edge[1] + ", Weight: " + edge[2]);
        }
        int totalWeight = 0;
        for (Object[] edge : outputMst) {
            totalWeight += (Integer)edge[2];
        }
        System.out.println("最小全域木の合計重み: " + totalWeight);

        graphData.clear();
        inputList = new Object[][]{
            {"A", "B", 4}, {"C", "D", 4}, {"E", "F", 1}, {"F", "G", 1}
        };
        for (Object[] input : inputList) {
            graphData.addEdge((String)input[0], (String)input[1], (Integer)input[2]);
        }
        System.out.println("\nグラフの頂点: " + graphData.getVertices());
        System.out.println("グラフの辺 (重み付き): " + Arrays.deepToString(graphData.getEdges().toArray()));
        outputMst = graphData.getMst();
        for (Object[] edge : outputMst) {
            System.out.println("Edge: " + edge[0] + " - " + edge[1] + ", Weight: " + edge[2]);
        }
        totalWeight = 0;
        for (Object[] edge : outputMst) {
            totalWeight += (Integer)edge[2];
        }
        System.out.println("最小全域木の合計重み: " + totalWeight);

        graphData.clear();
        inputList = new Object[][]{
            {"A", "B", 4}, {"B", "C", 3}, {"D", "E", 5}
        };
        for (Object[] input : inputList) {
            graphData.addEdge((String)input[0], (String)input[1], (Integer)input[2]);
        }
        System.out.println("\nグラフの頂点: " + graphData.getVertices());
        System.out.println("グラフの辺 (重み付き): " + Arrays.deepToString(graphData.getEdges().toArray()));
        outputMst = graphData.getMst();
        for (Object[] edge : outputMst) {
            System.out.println("Edge: " + edge[0] + " - " + edge[1] + ", Weight: " + edge[2]);
        }
        totalWeight = 0;
        for (Object[] edge : outputMst) {
            totalWeight += (Integer)edge[2];
        }
        System.out.println("最小全域木の合計重み: " + totalWeight);

        graphData.clear();
        inputList = new Object[][]{};
        for (Object[] input : inputList) {
            graphData.addEdge((String)input[0], (String)input[1], (Integer)input[2]);
        }
        System.out.println("\nグラフの頂点: " + graphData.getVertices());
        System.out.println("グラフの辺 (重み付き): " + Arrays.deepToString(graphData.getEdges().toArray()));
        outputMst = graphData.getMst();
        for (Object[] edge : outputMst) {
            System.out.println("Edge: " + edge[0] + " - " + edge[1] + ", Weight: " + edge[2]);
        }
        totalWeight = 0;
        for (Object[] edge : outputMst) {
            totalWeight += (Integer)edge[2];
        }
        System.out.println("最小全域木の合計重み: " + totalWeight);

        System.out.println("\nPrims TEST <----- end");
    }
}