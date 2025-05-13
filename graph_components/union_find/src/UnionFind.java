// Java
// グラフの連結成分: Union-Find

import java.util.*;

class GraphData {
    // 隣接ノードとその辺の重みを格納します。
    // キーは頂点、値はその頂点に隣接する頂点と重みのリストです。
    private Map<String, List<Pair<String, Integer>>> data;

    // 頂点と重みのペアを表すクラス
    private static class Pair<K, V> {
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
    }

    // エッジを表すクラス（get_edges メソッド用）
    private static class Edge {
        private final String u;
        private final String v;
        private final int weight;

        public Edge(String u, String v, int weight) {
            this.u = u;
            this.v = v;
            this.weight = weight;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            Edge edge = (Edge) obj;
            return weight == edge.weight && 
                   ((u.equals(edge.u) && v.equals(edge.v)) || 
                    (u.equals(edge.v) && v.equals(edge.u)));
        }

        @Override
        public int hashCode() {
            // 無向グラフなので、uとvの順序に依存しないハッシュコードを生成
            return Objects.hash(
                    Math.min(u.hashCode(), v.hashCode()),
                    Math.max(u.hashCode(), v.hashCode()),
                    weight);
        }
    }

    public GraphData() {
        data = new HashMap<>();
    }

    public Map<String, List<Pair<String, Integer>>> get() {
        // グラフの内部データを取得します。
        return data;
    }

    public List<String> getVertices() {
        // グラフの全頂点をリストとして返します。
        return new ArrayList<>(data.keySet());
    }

    public List<Object[]> getEdges() {
        // グラフの全辺をリストとして返します。
        // 無向グラフの場合、[u, v, weight] の形式で返します。
        // 重複を避けるためにセットを使用します。
        Set<Edge> edges = new HashSet<>();
        for (String vertex : data.keySet()) {
            for (Pair<String, Integer> neighbor : data.get(vertex)) {
                // 辺を正規化してセットに追加（小さい方の頂点を最初にするなど）
                String[] sortedVertices = {vertex, neighbor.getFirst()};
                Arrays.sort(sortedVertices);
                edges.add(new Edge(sortedVertices[0], sortedVertices[1], neighbor.getSecond()));
            }
        }

        // Edge オブジェクトから配列への変換
        List<Object[]> result = new ArrayList<>();
        for (Edge edge : edges) {
            result.add(new Object[]{edge.u, edge.v, edge.weight});
        }
        return result;
    }

    public List<Pair<String, Integer>> getNeighbors(String vertex) {
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
            for (Pair<String, Integer> neighbor : data.get(vertex1)) {
                if (neighbor.getFirst().equals(vertex2)) {
                    return neighbor.getSecond();
                }
            }
        }
        return null; // 辺が存在しない場合
    }

    public List<Pair<String, Integer>> getVertice(String vertex) {
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
            for (Pair<String, Integer> neighbor : data.get(vertex1)) {
                if (neighbor.getFirst().equals(vertex2)) {
                    return true;
                }
            }
        }
        // どちらかの頂点が存在しない場合や辺が見つからない場合
        return false;
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

        // vertex1 -> vertex2 の辺を追加（重み付き）
        boolean edgeExistsV1V2 = false;
        for (int i = 0; i < data.get(vertex1).size(); i++) {
            Pair<String, Integer> pair = data.get(vertex1).get(i);
            if (pair.getFirst().equals(vertex2)) {
                // 既に存在する場合は重みを更新（既存のペアを削除し、新しいペアを追加）
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
            if (pair.getFirst().equals(vertex1)) {
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

    public boolean removeVertex(String vertex) {
        // 頂点とそれに関連する辺を削除します。
        if (data.containsKey(vertex)) {
            // この頂点への参照を他の頂点の隣接リストから削除する
            for (String v : data.keySet()) {
                List<Pair<String, Integer>> neighbors = data.get(v);
                neighbors.removeIf(pair -> pair.getFirst().equals(vertex));
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
            data.get(vertex1).removeIf(pair -> pair.getFirst().equals(vertex2));
            if (data.get(vertex1).size() < originalLenV1) {
                removed = true;
            }

            // vertex2 から vertex1 への辺を削除
            int originalLenV2 = data.get(vertex2).size();
            data.get(vertex2).removeIf(pair -> pair.getFirst().equals(vertex1));
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

    public List<List<String>> getConnectedComponents() {
        if (data.isEmpty()) {
            return new ArrayList<>(); // 空のグラフの場合は空リストを返す
        }

        // Union-Findのためのデータ構造を初期化
        // parent[i] は要素 i の親を示す
        // size[i] は要素 i を根とする集合のサイズを示す (Union by Size用)
        Map<String, String> parent = new HashMap<>();
        Map<String, Integer> size = new HashMap<>();

        // 各頂点を初期状態では自分自身の集合に属させ、サイズを1とする
        List<String> vertices = getVertices();
        for (String vertex : vertices) {
            parent.put(vertex, vertex);
            size.put(vertex, 1);
        }

        // 経路圧縮 (Path Compression) を伴う Find 操作
        // vの親がv自身でなければ、根を探しにいく
        String find(String v) {
            if (!parent.get(v).equals(v)) {
                // 見つけた根をvの直接の親として記録 (経路圧縮)
                parent.put(v, find(parent.get(v)));
            }
            return parent.get(v); // 最終的に根を返す
        }

        // Union by Size を伴う Union 操作
        boolean union(String u, String v) {
            String rootU = find(u);
            String rootV = find(v);

            // 根が同じ場合は、すでに同じ集合に属しているので何もしない
            if (!rootU.equals(rootV)) {
                // より小さいサイズの木を大きいサイズの木に結合する
                if (size.get(rootU) < size.get(rootV)) {
                    parent.put(rootU, rootV);
                    size.put(rootV, size.get(rootV) + size.get(rootU));
                } else {
                    parent.put(rootV, rootU);
                    size.put(rootU, size.get(rootU) + size.get(rootV));
                }
                return true; // 結合が行われた
            }
            return false; // 結合は行われなかった
        }

        // グラフの全ての辺に対してUnion操作を行い、連結成分をマージする
        for (Object[] edge : getEdges()) {
            String u = (String) edge[0];
            String v = (String) edge[1];
            union(u, v);
        }

        // 連結成分をグループ化する
        // 根をキーとして、その根に属する頂点のリストを値とする辞書を作成
        Map<String, List<String>> components = new HashMap<>();
        for (String vertex : vertices) {
            String root = find(vertex); // 各頂点の最終的な根を見つける
            if (!components.containsKey(root)) {
                components.put(root, new ArrayList<>());
            }
            components.get(root).add(vertex);
        }

        // 連結成分のリスト（値の部分）を返す
        return new ArrayList<>(components.values());
    }
}

public class UnionFind {
    public static void main(String[] args) {
        System.out.println("UnionFind TEST -----> start");

        System.out.println("\nnew");
        GraphData graphData = new GraphData();
        System.out.println("  現在のデータ: " + graphData.get());

        System.out.println("\nadd_edge");
        graphData.clear();
        List<Object[]> inputList = new ArrayList<>();
        inputList.add(new Object[]{"A", "B", 4});
        inputList.add(new Object[]{"B", "C", 3});
        inputList.add(new Object[]{"B", "D", 2});
        inputList.add(new Object[]{"D", "A", 1});
        inputList.add(new Object[]{"A", "C", 2});
        inputList.add(new Object[]{"B", "D", 2});
        for (Object[] input : inputList) {
            System.out.println("  入力値: " + Arrays.toString(input));
            boolean output = graphData.addEdge((String)input[0], (String)input[1], (Integer)input[2]);
            System.out.println("  出力値: " + output);
        }
        System.out.println("  現在のデータ: " + graphData.get());
        System.out.println("\nget_connected_components");
        List<List<String>> output = graphData.getConnectedComponents();
        System.out.println("  連結成分: " + output);

        System.out.println("\nadd_edge");
        graphData.clear();
        inputList = new ArrayList<>();
        inputList.add(new Object[]{"A", "B", 4});
        inputList.add(new Object[]{"C", "D", 4});
        inputList.add(new Object[]{"E", "F", 1});
        inputList.add(new Object[]{"F", "G", 1});
        for (Object[] input : inputList) {
            System.out.println("  入力値: " + Arrays.toString(input));
            boolean output = graphData.addEdge((String)input[0], (String)input[1], (Integer)input[2]);
            System.out.println("  出力値: " + output);
        }
        System.out.println("  現在のデータ: " + graphData.get());
        System.out.println("\nget_connected_components");
        output = graphData.getConnectedComponents();
        System.out.println("  連結成分: " + output);

        System.out.println("\nadd_edge");
        graphData.clear();        
        inputList = new ArrayList<>();
        inputList.add(new Object[]{"A", "B", 4});
        inputList.add(new Object[]{"B", "C", 3});
        inputList.add(new Object[]{"D", "E", 5});
        for (Object[] input : inputList) {
            System.out.println("  入力値: " + Arrays.toString(input));
            boolean output = graphData.addEdge((String)input[0], (String)input[1], (Integer)input[2]);
            System.out.println("  出力値: " + output);
        }
        System.out.println("  現在のデータ: " + graphData.get());
        System.out.println("\nget_connected_components");
        output = graphData.getConnectedComponents();
        System.out.println("  連結成分: " + output);

        System.out.println("\nadd_edge");
        graphData.clear();
        inputList = new ArrayList<>();
        for (Object[] input : inputList) {
            System.out.println("  入力値: " + Arrays.toString(input));
            boolean output = graphData.addEdge((String)input[0], (String)input[1], (Integer)input[2]);
            System.out.println("  出力値: " + output);
        }
        System.out.println("  現在のデータ: " + graphData.get());
        System.out.println("\nget_connected_components");
        output = graphData.getConnectedComponents();
        System.out.println("  連結成分: " + output);

        System.out.println("\nUnionFind TEST <----- end");
    }
}