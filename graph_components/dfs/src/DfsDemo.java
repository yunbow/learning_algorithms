// Java
// グラフの連結成分: DFS

import java.util.*;

public class DfsDemo {
    private Map<String, List<Pair<String, Integer>>> data;

    public DfsDemo() {
        data = new HashMap<>();
    }

    public Map<String, List<Pair<String, Integer>>> get() {
        return data;
    }

    public List<String> getVertices() {
        return new ArrayList<>(data.keySet());
    }

    public List<Edge> getEdges() {
        Set<Edge> edges = new HashSet<>();
        for (String vertex : data.keySet()) {
            for (Pair<String, Integer> neighbor : data.get(vertex)) {
                String[] sorted = new String[]{vertex, neighbor.first};
                Arrays.sort(sorted);
                edges.add(new Edge(sorted[0], sorted[1], neighbor.second));
            }
        }
        return new ArrayList<>(edges);
    }

    public List<Pair<String, Integer>> getNeighbors(String vertex) {
        return data.getOrDefault(vertex, null);
    }

    public Integer getEdgeWeight(String vertex1, String vertex2) {
        if (data.containsKey(vertex1)) {
            for (Pair<String, Integer> neighbor : data.get(vertex1)) {
                if (neighbor.first.equals(vertex2)) {
                    return neighbor.second;
                }
            }
        }
        return null;
    }

    public List<Pair<String, Integer>> getVertice(String vertex) {
        if (data.containsKey(vertex)) {
            return data.get(vertex);
        }
        System.out.println("ERROR: " + vertex + "は範囲外です");
        return null;
    }

    public boolean getEdge(String vertex1, String vertex2) {
        if (data.containsKey(vertex1) && data.containsKey(vertex2)) {
            for (Pair<String, Integer> neighbor : data.get(vertex1)) {
                if (neighbor.first.equals(vertex2)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean addVertex(String vertex) {
        if (!data.containsKey(vertex)) {
            data.put(vertex, new ArrayList<>());
            return true;
        }
        return true;
    }

    public boolean addEdge(String vertex1, String vertex2, int weight) {
        addVertex(vertex1);
        addVertex(vertex2);

        boolean v1v2Exists = false;
        for (int i = 0; i < data.get(vertex1).size(); i++) {
            Pair<String, Integer> neighbor = data.get(vertex1).get(i);
            if (neighbor.first.equals(vertex2)) {
                data.get(vertex1).set(i, new Pair<>(vertex2, weight));
                v1v2Exists = true;
                break;
            }
        }
        if (!v1v2Exists) {
            data.get(vertex1).add(new Pair<>(vertex2, weight));
        }

        boolean v2v1Exists = false;
        for (int i = 0; i < data.get(vertex2).size(); i++) {
            Pair<String, Integer> neighbor = data.get(vertex2).get(i);
            if (neighbor.first.equals(vertex1)) {
                data.get(vertex2).set(i, new Pair<>(vertex1, weight));
                v2v1Exists = true;
                break;
            }
        }
        if (!v2v1Exists) {
            data.get(vertex2).add(new Pair<>(vertex1, weight));
        }

        return true;
    }

    public boolean removeVertex(String vertex) {
        if (data.containsKey(vertex)) {
            for (String v : data.keySet()) {
                data.get(v).removeIf(neighbor -> neighbor.first.equals(vertex));
            }
            data.remove(vertex);
            return true;
        }
        System.out.println("ERROR: " + vertex + "は範囲外です");
        return false;
    }

    public boolean removeEdge(String vertex1, String vertex2) {
        if (data.containsKey(vertex1) && data.containsKey(vertex2)) {
            boolean removed = false;
            int originalSizeV1 = data.get(vertex1).size();
            data.get(vertex1).removeIf(neighbor -> neighbor.first.equals(vertex2));
            if (data.get(vertex1).size() < originalSizeV1) {
                removed = true;
            }

            int originalSizeV2 = data.get(vertex2).size();
            data.get(vertex2).removeIf(neighbor -> neighbor.first.equals(vertex1));
            if (data.get(vertex2).size() < originalSizeV2) {
                removed = true;
            }

            return removed;
        }
        System.out.println("ERROR: " + vertex1 + " または " + vertex2 + " は範囲外です");
        return false;
    }

    public boolean isEmpty() {
        return data.isEmpty();
    }

    public int size() {
        return data.size();
    }

    public boolean clear() {
        data.clear();
        return true;
    }

    private void dfs(String vertex, Set<String> visited, List<String> currentComponent) {
        visited.add(vertex);
        currentComponent.add(vertex);

        for (Pair<String, Integer> neighborInfo : data.getOrDefault(vertex, new ArrayList<>())) {
            String neighborVertex = neighborInfo.first;
            if (!visited.contains(neighborVertex)) {
                dfs(neighborVertex, visited, currentComponent);
            }
        }
    }

    public List<List<String>> getConnectedComponents() {
        Set<String> visited = new HashSet<>();
        List<List<String>> connectedComponents = new ArrayList<>();

        for (String vertex : getVertices()) {
            if (!visited.contains(vertex)) {
                List<String> currentComponent = new ArrayList<>();
                dfs(vertex, visited, currentComponent);
                connectedComponents.add(currentComponent);
            }
        }

        return connectedComponents;
    }

    public static void main(String[] args) {
        System.out.println("Dfs TEST -----> start");

        System.out.println("\nnew");
        DfsDemo graphData = new DfsDemo();
        System.out.println("  現在のデータ: " + graphData.get());

        System.out.println("\nadd_edge");
        graphData.clear();
        Object[][] inputList = {
            {"A", "B", 4}, {"B", "C", 3}, {"B", "D", 2}, 
            {"D", "A", 1}, {"A", "C", 2}, {"B", "D", 2}
        };
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
        inputList = new Object[][] {
            {"A", "B", 4}, {"C", "D", 4}, {"E", "F", 1}, {"F", "G", 1}
        };
        for (Object[] input : inputList) {
            System.out.println("  入力値: " + Arrays.toString(input));
            boolean output2 = graphData.addEdge((String)input[0], (String)input[1], (Integer)input[2]);
            System.out.println("  出力値: " + output2);
        }
        System.out.println("  現在のデータ: " + graphData.get());
        System.out.println("\nget_connected_components");
        output = graphData.getConnectedComponents();
        System.out.println("  連結成分: " + output);

        System.out.println("\nadd_edge");
        graphData.clear();
        inputList = new Object[][] {
            {"A", "B", 4}, {"B", "C", 3}, {"D", "E", 5}
        };
        for (Object[] input : inputList) {
            System.out.println("  入力値: " + Arrays.toString(input));
            boolean output3 = graphData.addEdge((String)input[0], (String)input[1], (Integer)input[2]);
            System.out.println("  出力値: " + output3);
        }
        System.out.println("  現在のデータ: " + graphData.get());
        System.out.println("\nget_connected_components");
        output = graphData.getConnectedComponents();
        System.out.println("  連結成分: " + output);

        System.out.println("\nadd_edge");
        graphData.clear();
        inputList = new Object[0][0];
        for (Object[] input : inputList) {
            System.out.println("  入力値: " + Arrays.toString(input));
            boolean output4 = graphData.addEdge((String)input[0], (String)input[1], (Integer)input[2]);
            System.out.println("  出力値: " + output4);
        }
        System.out.println("  現在のデータ: " + graphData.get());
        System.out.println("\nget_connected_components");
        output = graphData.getConnectedComponents();
        System.out.println("  連結成分: " + output);

        System.out.println("Dfs TEST <----- end");
    }

    // Helper classes
    public static class Pair<F, S> {
        public final F first;
        public final S second;

        public Pair(F first, S second) {
            this.first = first;
            this.second = second;
        }

        @Override
        public String toString() {
            return "(" + first + ", " + second + ")";
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Pair<?, ?> pair = (Pair<?, ?>) o;
            return Objects.equals(first, pair.first) && Objects.equals(second, pair.second);
        }

        @Override
        public int hashCode() {
            return Objects.hash(first, second);
        }
    }

    public static class Edge {
        public final String vertex1;
        public final String vertex2;
        public final int weight;

        public Edge(String vertex1, String vertex2, int weight) {
            this.vertex1 = vertex1;
            this.vertex2 = vertex2;
            this.weight = weight;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Edge edge = (Edge) o;
            return weight == edge.weight && 
                   ((Objects.equals(vertex1, edge.vertex1) && Objects.equals(vertex2, edge.vertex2)) ||
                    (Objects.equals(vertex1, edge.vertex2) && Objects.equals(vertex2, edge.vertex1)));
        }

        @Override
        public int hashCode() {
            return Objects.hash(vertex1, vertex2, weight);
        }

        @Override
        public String toString() {
            return "(" + vertex1 + ", " + vertex2 + ", " + weight + ")";
        }
    }
}
