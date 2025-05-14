// Java
// グラフの連結成分: BFS

import java.util.*;

class GraphData {
    // 隣接ノードとその辺の重みを格納するマップ
    private Map<String, List<AbstractMap.SimpleEntry<String, Integer>>> data;

    public GraphData() {
        data = new HashMap<>();
    }

    public Map<String, List<AbstractMap.SimpleEntry<String, Integer>>> get() {
        return data;
    }

    public List<String> getVertices() {
        return new ArrayList<>(data.keySet());
    }

    public List<AbstractMap.SimpleEntry<String, Integer>> getNeighbors(String vertex) {
        return data.getOrDefault(vertex, null);
    }

    public Integer getEdgeWeight(String vertex1, String vertex2) {
        if (data.containsKey(vertex1)) {
            for (AbstractMap.SimpleEntry<String, Integer> neighbor : data.get(vertex1)) {
                if (neighbor.getKey().equals(vertex2)) {
                    return neighbor.getValue();
                }
            }
        }
        return null;
    }

    public List<AbstractMap.SimpleEntry<String, Integer>> getVertice(String vertex) {
        if (data.containsKey(vertex)) {
            return data.get(vertex);
        } else {
            System.out.println("ERROR: " + vertex + "は範囲外です");
            return null;
        }
    }

    public boolean getEdge(String vertex1, String vertex2) {
        if (data.containsKey(vertex1) && data.containsKey(vertex2)) {
            for (AbstractMap.SimpleEntry<String, Integer> neighbor : data.get(vertex1)) {
                if (neighbor.getKey().equals(vertex2)) {
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

        boolean edgeExistsV1V2 = false;
        for (int i = 0; i < data.get(vertex1).size(); i++) {
            AbstractMap.SimpleEntry<String, Integer> neighbor = data.get(vertex1).get(i);
            if (neighbor.getKey().equals(vertex2)) {
                data.get(vertex1).set(i, new AbstractMap.SimpleEntry<>(vertex2, weight));
                edgeExistsV1V2 = true;
                break;
            }
        }
        if (!edgeExistsV1V2) {
            data.get(vertex1).add(new AbstractMap.SimpleEntry<>(vertex2, weight));
        }

        boolean edgeExistsV2V1 = false;
        for (int i = 0; i < data.get(vertex2).size(); i++) {
            AbstractMap.SimpleEntry<String, Integer> neighbor = data.get(vertex2).get(i);
            if (neighbor.getKey().equals(vertex1)) {
                data.get(vertex2).set(i, new AbstractMap.SimpleEntry<>(vertex1, weight));
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
        if (data.containsKey(vertex)) {
            for (String v : data.keySet()) {
                data.get(v).removeIf(neighbor -> neighbor.getKey().equals(vertex));
            }
            data.remove(vertex);
            return true;
        } else {
            System.out.println("ERROR: " + vertex + " は範囲外です");
            return false;
        }
    }

    public boolean removeEdge(String vertex1, String vertex2) {
        if (data.containsKey(vertex1) && data.containsKey(vertex2)) {
            boolean removed = false;

            int originalLenV1 = data.get(vertex1).size();
            data.get(vertex1).removeIf(neighbor -> neighbor.getKey().equals(vertex2));
            if (data.get(vertex1).size() < originalLenV1) {
                removed = true;
            }

            int originalLenV2 = data.get(vertex2).size();
            data.get(vertex2).removeIf(neighbor -> neighbor.getKey().equals(vertex1));
            if (data.get(vertex2).size() < originalLenV2) {
                removed = true;
            }

            return removed;
        } else {
            System.out.println("ERROR: " + vertex1 + " または " + vertex2 + " は範囲外です");
            return false;
        }
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

    public List<List<String>> getConnectedComponents() {
        Set<String> visited = new HashSet<>();
        List<List<String>> allComponents = new ArrayList<>();

        List<String> vertices = getVertices();

        for (String vertex : vertices) {
            if (!visited.contains(vertex)) {
                List<String> currentComponent = new ArrayList<>();
                Queue<String> queue = new LinkedList<>();
                queue.add(vertex);
                visited.add(vertex);
                currentComponent.add(vertex);

                while (!queue.isEmpty()) {
                    String u = queue.poll();

                    List<AbstractMap.SimpleEntry<String, Integer>> neighborsWithWeight = getNeighbors(u);

                    if (neighborsWithWeight != null) {
                        for (AbstractMap.SimpleEntry<String, Integer> neighborEntry : neighborsWithWeight) {
                            String neighbor = neighborEntry.getKey();
                            if (!visited.contains(neighbor)) {
                                visited.add(neighbor);
                                queue.add(neighbor);
                                currentComponent.add(neighbor);
                            }
                        }
                    }
                }

                allComponents.add(currentComponent);
            }
        }

        return allComponents;
    }
}

public class BfsDemo {
    public static void main(String[] args) {
        System.out.println("Bfs TEST -----> start");

        System.out.println("\nnew");
        GraphData graphData = new GraphData();
        System.out.println("  現在のデータ: " + graphData.get());

        System.out.println("\nadd_edge");
        graphData.clear();
        Object[][] inputList = {
            {"A", "B", 4}, {"B", "C", 3}, {"B", "D", 2}, 
            {"D", "A", 1}, {"A", "C", 2}, {"B", "D", 2}
        };
        for (Object[] input : inputList) {
            System.out.println("  入力値: " + Arrays.toString(input));
            boolean output = graphData.addEdge((String)input[0], (String)input[1], (int)input[2]);
            System.out.println("  出力値: " + output);
        }
        System.out.println("  現在のデータ: " + graphData.get());
        System.out.println("\nget_connected_components");
        List<List<String>> output = graphData.getConnectedComponents();
        System.out.println("  連結成分: " + output);
    }
}