// Java
// データ構造: グラフ (Graph)

import java.util.*;

class GraphData {
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

    public List<AbstractMap.SimpleEntry<String[], Integer>> getEdges() {
        // グラフの全辺をリストとして返します。
        // 無向グラフの場合、([u, v], weight) の形式で返します。
        // 重複を避けるためにセットを使用します。
        Set<AbstractMap.SimpleEntry<String[], Integer>> edges = new HashSet<>();
        for (String vertex : data.keySet()) {
            for (AbstractMap.SimpleEntry<String, Integer> neighbor : data.get(vertex)) {
                // 辺を正規化してセットに追加 (小さい方の頂点を最初にするなど)
                String[] sortedEdge = new String[]{vertex, neighbor.getKey()};
                Arrays.sort(sortedEdge);
                edges.add(new AbstractMap.SimpleEntry<>(sortedEdge, neighbor.getValue()));
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

    public Integer getEdgeWeight(String vertex1, String vertex2) {
        // 指定された2つの頂点間の辺の重みを返します。
        // 辺が存在しない場合はNullを返します。
        if (data.containsKey(vertex1) && data.containsKey(vertex2)) {
            for (AbstractMap.SimpleEntry<String, Integer> neighbor : data.get(vertex1)) {
                if (neighbor.getKey().equals(vertex2)) {
                    return neighbor.getValue();
                }
            }
        }
        return null; // 辺が存在しない場合
    }

    public List<AbstractMap.SimpleEntry<String, Integer>> getVertice(String vertex) {
        // 頂点がグラフに存在するか確認する
        if (data.containsKey(vertex)) {
            // 存在する場合は、その頂点の隣接リスト（関連する値）を返す
            return data.get(vertex);
        } else {
            // 存在しない場合はメッセージを表示し、Nullを返す
            System.out.println("ERROR: " + vertex + "は範囲外です");
            return null;
        }
    }

    public boolean getEdge(String vertex1, String vertex2) {
        // 指定された2つの頂点間に辺が存在するかを確認する
        // 両方の頂点がグラフに存在する必要がある
        if (data.containsKey(vertex1) && data.containsKey(vertex2)) {
            // vertex1の隣接リストにvertex2が含まれているかを確認
            for (AbstractMap.SimpleEntry<String, Integer> neighbor : data.get(vertex1)) {
                if (neighbor.getKey().equals(vertex2)) {
                    return true;
                }
            }
        }
        // どちらかの頂点が存在しない場合か、辺が存在しない場合はfalse
        return false;
    }

    public boolean addVertex(String vertex) {
        // 新しい頂点をグラフに追加します。
        if (!data.containsKey(vertex)) {
            data.put(vertex, new ArrayList<>());
        }
        // 既に存在する場合も追加しないがTrueを返す（変更なしでも成功とみなす）
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
            if (data.get(vertex1).get(i).getKey().equals(vertex2)) {
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
            if (data.get(vertex2).get(i).getKey().equals(vertex1)) {
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

    public boolean removeVertex(String vertex) {
        // 頂点とそれに関連する辺を削除する
        if (data.containsKey(vertex)) {
            // この頂点への参照を他の頂点の隣接リストから削除する
            for (String v : data.keySet()) {
                List<AbstractMap.SimpleEntry<String, Integer>> neighbors = data.get(v);
                neighbors.removeIf(entry -> entry.getKey().equals(vertex));
            }
            // 頂点自体を削除する
            data.remove(vertex);
            return true;
        } else {
            System.out.println("ERROR: " + vertex + " は範囲外です");
            return false;
        }
    }

    public boolean removeEdge(String vertex1, String vertex2) {
        // 両頂点間の辺を削除します。
        if (data.containsKey(vertex1) && data.containsKey(vertex2)) {
            boolean removed = false;
            // vertex1 から vertex2 への辺を削除
            int originalLenV1 = data.get(vertex1).size();
            data.get(vertex1).removeIf(entry -> entry.getKey().equals(vertex2));
            if (data.get(vertex1).size() < originalLenV1) {
                removed = true;
            }

            // vertex2 から vertex1 への辺を削除
            int originalLenV2 = data.get(vertex2).size();
            data.get(vertex2).removeIf(entry -> entry.getKey().equals(vertex1));
            if (data.get(vertex2).size() < originalLenV2) {
                removed = true;
            }

            return removed; // 少なくとも片方向が削除されたか
        } else {
            System.out.println("ERROR: " + vertex1 + " または " + vertex2 + " は範囲外です");
            return false;
        }
    }

    public boolean isEmpty() {
        // グラフが空かどうか
        return data.isEmpty();
    }

    public int size() {
        // グラフの頂点数を返す
        return data.size();
    }

    public boolean clear() {
        // グラフを空にする
        data.clear();
        return true;
    }
}

public class GraphDemo {
    public static void main(String[] args) {
        System.out.println("Graph TEST -----> start");

        System.out.println("\nnew");
        GraphData graphData = new GraphData();
        System.out.println("  現在のデータ: " + graphData.get());

        System.out.println("\nis_empty");
        boolean output = graphData.isEmpty();
        System.out.println("  出力値: " + output);

        System.out.println("\nsize");
        int sizeOutput = graphData.size();
        System.out.println("  出力値: " + sizeOutput);

        String[] inputList = {"A", "B", "C"};
        for (String input : inputList) {
            System.out.println("\nadd_vertex");
            System.out.println("  入力値: " + input);
            boolean addVertexOutput = graphData.addVertex(input);
            System.out.println("  出力値: " + addVertexOutput);
        }
        System.out.println("  現在のデータ: " + graphData.get());

        System.out.println("\nget_vertices");
        List<String> verticesOutput = graphData.getVertices();
        System.out.println("  出力値: " + verticesOutput);

        System.out.println("\nsize");
        sizeOutput = graphData.size();
        System.out.println("  出力値: " + sizeOutput);

        System.out.println("\nadd_edge");
        Object[][] edgeInputList = {{"A", "B", 4}, {"B", "C", 2}, {"C", "A", 3}};
        for (Object[] input : edgeInputList) {
            System.out.println("  入力値: " + Arrays.toString(input));
            boolean addEdgeOutput = graphData.addEdge((String)input[0], (String)input[1], (Integer)input[2]);
            System.out.println("  出力値: " + addEdgeOutput);
        }
        System.out.println("  現在のデータ: " + graphData.get());

        System.out.println("\nget_vertices");
        verticesOutput = graphData.getVertices();
        System.out.println("  出力値: " + verticesOutput);

        System.out.println("\nget_edges");
        List<AbstractMap.SimpleEntry<String[], Integer>> edgesOutput = graphData.getEdges();
        System.out.println("  出力値: " + edgesOutput);

        System.out.println("\nsize");
        sizeOutput = graphData.size();
        System.out.println("  出力値: " + sizeOutput);

        System.out.println("\nget_vertice");
        String verticeInput = "B";
        System.out.println("  入力値: '" + verticeInput + "'");
        List<AbstractMap.SimpleEntry<String, Integer>> verticeOutput = graphData.getVertice(verticeInput);
        System.out.println("  出力値: " + verticeOutput);

        System.out.println("\nget_vertice");
        verticeInput = "E";
        System.out.println("  入力値: '" + verticeInput + "'");
        verticeOutput = graphData.getVertice(verticeInput);
        System.out.println("  出力値: " + verticeOutput);

        System.out.println("\nremove_edge");
        String[] removeEdgeInput = {"A", "B"};
        System.out.println("  入力値: " + Arrays.toString(removeEdgeInput));
        boolean removeEdgeOutput = graphData.removeEdge(removeEdgeInput[0], removeEdgeInput[1]);
        System.out.println("  出力値: " + removeEdgeOutput);
        System.out.println("  現在のデータ: " + graphData.get());

        System.out.println("\nremove_edge");
        removeEdgeInput = new String[]{"A", "C"};
        System.out.println("  入力値: " + Arrays.toString(removeEdgeInput));
        removeEdgeOutput = graphData.removeEdge(removeEdgeInput[0], removeEdgeInput[1]);
        System.out.println("  出力値: " + removeEdgeOutput);
        System.out.println("  現在のデータ: " + graphData.get());

        System.out.println("\nget_edges");
        edgesOutput = graphData.getEdges();
        System.out.println("  出力値: " + edgesOutput);

        System.out.println("\nremove_vertex");
        String removeVertexInput = "B";
        System.out.println("  入力値: " + removeVertexInput);
        boolean removeVertexOutput = graphData.removeVertex(removeVertexInput);
        System.out.println("  出力値: " + removeVertexOutput);
        System.out.println("  現在のデータ: " + graphData.get());

        System.out.println("\nremove_vertex");
        removeVertexInput = "Z";
        System.out.println("  入力値: " + removeVertexInput);
        removeVertexOutput = graphData.removeVertex(removeVertexInput);
        System.out.println("  出力値: " + removeVertexOutput);
        System.out.println("  現在のデータ: " + graphData.get());

        System.out.println("\nget_vertices");
        verticesOutput = graphData.getVertices();
        System.out.println("  出力値: " + verticesOutput);

        System.out.println("\nget_edges");
        edgesOutput = graphData.getEdges();
        System.out.println("  出力値: " + edgesOutput);

        System.out.println("\nsize");
        sizeOutput = graphData.size();
        System.out.println("  出力値: " + sizeOutput);

        System.out.println("\nget_vertice");
        verticeInput = "B";
        System.out.println("  入力値: " + verticeInput);
        verticeOutput = graphData.getVertice(verticeInput);
        System.out.println("  出力値: " + verticeOutput);

        System.out.println("\nclear");
        boolean clearOutput = graphData.clear();
        System.out.println("  出力値: " + clearOutput);

        System.out.println("\nis_empty");
        output = graphData.isEmpty();
        System.out.println("  出力値: " + output);

        System.out.println("\nsize");
        sizeOutput = graphData.size();
        System.out.println("  出力値: " + sizeOutput);

        System.out.println("\nget_vertices");
        verticesOutput = graphData.getVertices();
        System.out.println("  出力値: " + verticesOutput);

        System.out.println("\nget_edges");
        edgesOutput = graphData.getEdges();
        System.out.println("  出力値: " + edgesOutput);

        System.out.println("\nGraph TEST <----- end");
    }
}