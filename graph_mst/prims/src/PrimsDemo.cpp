// C++
// グラフの最小全域木: プリム法 (Prim)

#include <iostream>
#include <vector>
#include <map>
#include <set>
#include <queue>
#include <limits>
#include <algorithm>
#include <tuple>

class GraphData {
private:
    // 隣接リストとしてグラフデータを格納します。
    // キーは頂点、値はその頂点に隣接する頂点とその辺の重みのペアのベクトルです。
    std::map<std::string, std::vector<std::pair<std::string, int>>> _data;

public:
    GraphData() {}

    // グラフの内部データを取得します。
    std::map<std::string, std::vector<std::pair<std::string, int>>>& get() {
        return _data;
    }

    // グラフの全頂点をベクトルとして返します。
    std::vector<std::string> get_vertices() {
        std::vector<std::string> vertices;
        for (const auto& pair : _data) {
            vertices.push_back(pair.first);
        }
        return vertices;
    }

    // グラフの全辺をリストとして返します。
    // 無向グラフの場合、(u, v, weight) の形式で返します。
    std::vector<std::tuple<std::string, std::string, int>> get_edges() {
        std::set<std::tuple<std::string, std::string, int>> edges;
        for (const auto& vertex_pair : _data) {
            for (const auto& neighbor_weight : vertex_pair.second) {
                std::string vertex = vertex_pair.first;
                std::string neighbor = neighbor_weight.first;
                int weight = neighbor_weight.second;
                
                // 辺を正規化してセットに追加 (小さい方の頂点を最初にするなど)
                std::tuple<std::string, std::string, int> edge;
                if (vertex < neighbor) {
                    edge = std::make_tuple(vertex, neighbor, weight);
                } else {
                    edge = std::make_tuple(neighbor, vertex, weight);
                }
                edges.insert(edge);
            }
        }
        return std::vector<std::tuple<std::string, std::string, int>>(edges.begin(), edges.end());
    }

    // 指定された頂点の隣接ノードと辺の重みのリストを返します。
    std::vector<std::pair<std::string, int>> get_neighbors(const std::string& vertex) {
        if (_data.find(vertex) != _data.end()) {
            return _data[vertex];
        }
        return std::vector<std::pair<std::string, int>>(); // 頂点が存在しない場合は空のベクトルを返す
    }

    // 指定された2つの頂点間の辺の重みを返します。
    // 辺が存在しない場合は-1を返します。
    int get_edge_weight(const std::string& vertex1, const std::string& vertex2) {
        if (_data.find(vertex1) != _data.end() && _data.find(vertex2) != _data.end()) {
            for (const auto& neighbor_weight : _data[vertex1]) {
                if (neighbor_weight.first == vertex2) {
                    return neighbor_weight.second;
                }
            }
        }
        return -1; // 辺が存在しない場合
    }

    // 新しい頂点をグラフに追加します。
    bool add_vertex(const std::string& vertex) {
        if (_data.find(vertex) == _data.end()) {
            _data[vertex] = std::vector<std::pair<std::string, int>>();
        }
        return true;
    }

    // 両頂点間に辺を追加します。重みを指定します。
    bool add_edge(const std::string& vertex1, const std::string& vertex2, int weight) {
        if (_data.find(vertex1) == _data.end()) {
            add_vertex(vertex1);
        }
        if (_data.find(vertex2) == _data.end()) {
            add_vertex(vertex2);
        }

        // vertex1 -> vertex2 の辺を追加（重み付き）
        bool edge_exists_v1v2 = false;
        for (auto& neighbor_weight : _data[vertex1]) {
            if (neighbor_weight.first == vertex2) {
                neighbor_weight.second = weight; // 既に存在する場合は重みを更新
                edge_exists_v1v2 = true;
                break;
            }
        }
        if (!edge_exists_v1v2) {
            _data[vertex1].push_back(std::make_pair(vertex2, weight));
        }

        // vertex2 -> vertex1 の辺を追加（重み付き）
        bool edge_exists_v2v1 = false;
        for (auto& neighbor_weight : _data[vertex2]) {
            if (neighbor_weight.first == vertex1) {
                neighbor_weight.second = weight; // 既に存在する場合は重みを更新
                edge_exists_v2v1 = true;
                break;
            }
        }
        if (!edge_exists_v2v1) {
            _data[vertex2].push_back(std::make_pair(vertex1, weight));
        }

        return true;
    }

    // グラフが空かどうかを返します。
    bool is_empty() {
        return _data.empty();
    }

    // グラフを空にします。
    bool clear() {
        _data.clear();
        return true;
    }

    // 最小全域木を計算します
    std::vector<std::tuple<std::string, std::string, int>> get_mst(const std::string* start_vertex = nullptr) {
        std::vector<std::string> vertices = get_vertices();
        if (vertices.empty()) {
            return {}; // グラフが空
        }

        std::string start;
        if (start_vertex == nullptr) {
            start = vertices[0];
        } else if (_data.find(*start_vertex) == _data.end()) {
            std::cout << "ERROR: 開始頂点 " << *start_vertex << " はグラフに存在しません。" << std::endl;
            return {};
        } else {
            start = *start_vertex;
        }

        // MSTに含まれる頂点のセット
        std::set<std::string> in_mst;
        
        // 優先度付きキュー (重み, 現在の頂点, 遷移元の頂点)
        // C++の優先度キューはデフォルトで最大ヒープなので、重みを負にして最小ヒープとして使用
        using PQElement = std::tuple<int, std::string, std::string>;
        std::priority_queue<PQElement, std::vector<PQElement>, std::greater<PQElement>> min_heap;
        
        // MSTを構成する辺のリスト
        std::vector<std::tuple<std::string, std::string, int>> mst_edges;
        
        // 各頂点への最小コスト（MSTに追加する際の辺の重み）
        std::map<std::string, int> min_cost;
        std::map<std::string, std::string> parent;
        
        // 初期化
        for (const auto& v : vertices) {
            min_cost[v] = std::numeric_limits<int>::max();
            parent[v] = "";
        }

        // 開始頂点の処理
        min_cost[start] = 0;
        min_heap.push(std::make_tuple(0, start, "")); // (コスト, 現在の頂点, 遷移元の頂点)

        while (!min_heap.empty()) {
            // 最小コストの辺を持つ頂点を取り出す
            int cost;
            std::string current_vertex, from_vertex;
            std::tie(cost, current_vertex, from_vertex) = min_heap.top();
            min_heap.pop();

            // 既にMSTに含まれている頂点であればスキップ
            if (in_mst.find(current_vertex) != in_mst.end()) {
                continue;
            }

            // 現在の頂点をMSTに追加
            in_mst.insert(current_vertex);

            // MSTに追加された辺を記録 (開始頂点以外)
            if (!from_vertex.empty()) {
                // from_vertex から current_vertex への辺の重みを取得
                int weight = get_edge_weight(from_vertex, current_vertex);
                if (weight != -1) {
                    // 辺を正規化して追加
                    if (from_vertex < current_vertex) {
                        mst_edges.push_back(std::make_tuple(from_vertex, current_vertex, weight));
                    } else {
                        mst_edges.push_back(std::make_tuple(current_vertex, from_vertex, weight));
                    }
                }
            }

            // 現在の頂点に隣接する頂点を調べ、MSTへの追加コストを更新
            auto neighbors_with_weight = get_neighbors(current_vertex);
            for (const auto& neighbor_weight : neighbors_with_weight) {
                std::string neighbor = neighbor_weight.first;
                int weight = neighbor_weight.second;
                
                // 隣接頂点がまだMSTに含まれておらず、現在のコストよりも小さい場合
                if (in_mst.find(neighbor) == in_mst.end() && weight < min_cost[neighbor]) {
                    min_cost[neighbor] = weight;
                    parent[neighbor] = current_vertex;
                    min_heap.push(std::make_tuple(weight, neighbor, current_vertex));
                }
            }
        }

        return mst_edges;
    }
};

int main() {
    std::cout << "Prims TEST -----> start" << std::endl;
    GraphData graph_data;

    graph_data.clear();
    std::vector<std::tuple<std::string, std::string, int>> inputList = {
        {"A", "B", 4}, {"B", "C", 3}, {"B", "D", 2}, {"D", "A", 1}, {"A", "C", 2}, {"B", "D", 2}
    };
    for (const auto& input : inputList) {
        graph_data.add_edge(std::get<0>(input), std::get<1>(input), std::get<2>(input));
    }
    std::cout << "\nグラフの頂点: ";
    for (const auto& vertex : graph_data.get_vertices()) {
        std::cout << vertex << " ";
    }
    std::cout << std::endl;
    
    std::cout << "グラフの辺 (重み付き): ";
    for (const auto& edge : graph_data.get_edges()) {
        std::cout << "(" << std::get<0>(edge) << ", " << std::get<1>(edge) << ", " << std::get<2>(edge) << ") ";
    }
    std::cout << std::endl;
    
    auto outputMst = graph_data.get_mst();
    for (const auto& edge : outputMst) {
        std::cout << "Edge: " << std::get<0>(edge) << " - " << std::get<1>(edge) << ", Weight: " << std::get<2>(edge) << std::endl;
    }
    int total_weight = 0;
    for (const auto& edge : outputMst) {
        total_weight += std::get<2>(edge);
    }
    std::cout << "最小全域木の合計重み: " << total_weight << std::endl;

    graph_data.clear();
    inputList = {
        {"A", "B", 4}, {"C", "D", 4}, {"E", "F", 1}, {"F", "G", 1}
    };
    for (const auto& input : inputList) {
        graph_data.add_edge(std::get<0>(input), std::get<1>(input), std::get<2>(input));
    }
    std::cout << "\nグラフの頂点: ";
    for (const auto& vertex : graph_data.get_vertices()) {
        std::cout << vertex << " ";
    }
    std::cout << std::endl;
    
    std::cout << "グラフの辺 (重み付き): ";
    for (const auto& edge : graph_data.get_edges()) {
        std::cout << "(" << std::get<0>(edge) << ", " << std::get<1>(edge) << ", " << std::get<2>(edge) << ") ";
    }
    std::cout << std::endl;
    
    outputMst = graph_data.get_mst();
    for (const auto& edge : outputMst) {
        std::cout << "Edge: " << std::get<0>(edge) << " - " << std::get<1>(edge) << ", Weight: " << std::get<2>(edge) << std::endl;
    }
    total_weight = 0;
    for (const auto& edge : outputMst) {
        total_weight += std::get<2>(edge);
    }
    std::cout << "最小全域木の合計重み: " << total_weight << std::endl;

    graph_data.clear();
    inputList = {
        {"A", "B", 4}, {"B", "C", 3}, {"D", "E", 5}
    };
    for (const auto& input : inputList) {
        graph_data.add_edge(std::get<0>(input), std::get<1>(input), std::get<2>(input));
    }
    std::cout << "\nグラフの頂点: ";
    for (const auto& vertex : graph_data.get_vertices()) {
        std::cout << vertex << " ";
    }
    std::cout << std::endl;
    
    std::cout << "グラフの辺 (重み付き): ";
    for (const auto& edge : graph_data.get_edges()) {
        std::cout << "(" << std::get<0>(edge) << ", " << std::get<1>(edge) << ", " << std::get<2>(edge) << ") ";
    }
    std::cout << std::endl;
    
    outputMst = graph_data.get_mst();
    for (const auto& edge : outputMst) {
        std::cout << "Edge: " << std::get<0>(edge) << " - " << std::get<1>(edge) << ", Weight: " << std::get<2>(edge) << std::endl;
    }
    total_weight = 0;
    for (const auto& edge : outputMst) {
        total_weight += std::get<2>(edge);
    }
    std::cout << "最小全域木の合計重み: " << total_weight << std::endl;

    graph_data.clear();
    std::cout << "\nグラフの頂点: ";
    for (const auto& vertex : graph_data.get_vertices()) {
        std::cout << vertex << " ";
    }
    std::cout << std::endl;
    
    std::cout << "グラフの辺 (重み付き): ";
    for (const auto& edge : graph_data.get_edges()) {
        std::cout << "(" << std::get<0>(edge) << ", " << std::get<1>(edge) << ", " << std::get<2>(edge) << ") ";
    }
    std::cout << std::endl;
    
    outputMst = graph_data.get_mst();
    for (const auto& edge : outputMst) {
        std::cout << "Edge: " << std::get<0>(edge) << " - " << std::get<1>(edge) << ", Weight: " << std::get<2>(edge) << std::endl;
    }
    total_weight = 0;
    for (const auto& edge : outputMst) {
        total_weight += std::get<2>(edge);
    }
    std::cout << "最小全域木の合計重み: " << total_weight << std::endl;

    std::cout << "\nPrims TEST <----- end" << std::endl;
    return 0;
}